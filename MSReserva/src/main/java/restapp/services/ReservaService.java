package restapp.services;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import restapp.DTOs.ConsultaItinerariosDTO;
import restapp.DTOs.ItinerarioDTO;
import restapp.DTOs.ReservaDTO;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;

public class ReservaService {
    private static final String HOST = "localhost";

    // EXCHANGE NAMES
    private static final String RESERVAS_EXCHANGE_NAME = "reservas";
    private static final String PAGAMENTOS_EXCHANGE_NAME = "pagamentos";
    private static final String BILHETES_EXCHANGE_NAME = "bilhetes";

    // ROUNTING KEYS
    private static final String RESERVA_CRIADA_RK = "reserva_criada";
    private static final String RESERVA_CANCELADA_RK = "reserva_cancelada";
    private static final String PAGAMENTO_RECUSADO_RK = "pagamento_recusado";
    private static final String PAGAMENTO_APROVADO_RK = "pagamento_aprovado";
    private static final String BILHETE_GERADO_RK = "bilhete_gerado";

    private final ConnectionFactory factory;
    private final  Connection connection;

    private WebClient itinearioWebClient = WebClient.builder().baseUrl("http://localhost:8081/api/itinerarios/").build();
    private WebClient pagamentoClient = WebClient.builder().baseUrl("http://localhost:8082/api/pagamentos").build();

    private final List<ReservaDTO> reservas = new ArrayList<ReservaDTO>();
    private final Map<String, List<Integer>> reservas_by_user = new HashMap<>();
    private final Map<String, List<Integer>> transacoes = new HashMap<>();
    private final Map<Integer, String> usuarioAssociadoTransacao = new HashMap<>();

    private final Map<String, SseEmitter> sseEmitterManager = new HashMap<>();

    public ReservaService() throws IOException, TimeoutException {
        factory = new ConnectionFactory();
        connection = factory.newConnection();

        Channel channel = connection.createChannel();

        String pagamento_aprovado_queue_name = channel.queueDeclare().getQueue();
        String pagamento_recusado_queue_name = channel.queueDeclare().getQueue();
        String bilhete_gerado_queue_name = channel.queueDeclare().getQueue();

        channel.exchangeDeclare(PAGAMENTOS_EXCHANGE_NAME, "direct");
        channel.queueBind(
                pagamento_aprovado_queue_name, PAGAMENTOS_EXCHANGE_NAME, PAGAMENTO_APROVADO_RK
        );

        channel.exchangeDeclare(PAGAMENTOS_EXCHANGE_NAME, "direct");
        channel.queueBind(
                pagamento_recusado_queue_name, PAGAMENTOS_EXCHANGE_NAME, PAGAMENTO_RECUSADO_RK
        );

        channel.exchangeDeclare(BILHETES_EXCHANGE_NAME, "direct");
        channel.queueBind(
                bilhete_gerado_queue_name, BILHETES_EXCHANGE_NAME, BILHETE_GERADO_RK
        );

        DeliverCallback PagamentoCallbackSuccess = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            processaMensagemPagamento(message,true);
        };
        DeliverCallback PagamentoCallbackFailure = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            processaMensagemPagamento(message,false);
        };
        DeliverCallback BilheteCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("** Bilhete Gerado : " + message);
        };

        channel.basicConsume(pagamento_aprovado_queue_name,true, PagamentoCallbackSuccess, consumerTag -> {});
        channel.basicConsume(pagamento_recusado_queue_name,true, PagamentoCallbackFailure, consumerTag -> {});
        channel.basicConsume(bilhete_gerado_queue_name,true, BilheteCallback, consumerTag -> {});
    }

    public ItinerarioDTO[] consultaItinerarios(ConsultaItinerariosDTO consultaItinerariosDTO){

        return itinearioWebClient.post()
                .uri("/consulta")
                .bodyValue(consultaItinerariosDTO)                     // o objeto será convertido para JSON automaticamente
                .retrieve()
                .bodyToMono(ItinerarioDTO[].class)           // resposta esperada
                .block();
    }

    public ItinerarioDTO[] consultaItinerarios(){

        return itinearioWebClient.get()
                .uri("/consulta")                    // o objeto será convertido para JSON automaticamente
                .retrieve()
                .bodyToMono(ItinerarioDTO[].class)           // resposta esperada
                .block();
    }

    public String efetuaReserva(String username, ReservaDTO reservaDTO){

        //TODO: validação da reserva

        reservas.add(reservaDTO);

        if(!reservas_by_user.containsKey(username))
            reservas_by_user.put(username, new ArrayList<Integer>());

        reservas_by_user.get(username).add(reservas.indexOf(reservaDTO));

        try {
            publicaEmReservaCriada(reservaDTO.serialize());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ItinerarioDTO itinerarioDTO[] = itinearioWebClient.get()
                .uri("/consulta")                   // o objeto será convertido para JSON automaticamente
                .retrieve()
                .bodyToMono(ItinerarioDTO[].class)           // resposta esperada
                .block();

        String valor_string = itinerarioDTO[reservaDTO.itinerario].valor_por_pessoa.split(" ")[1];
        valor_string = valor_string.replace(".","");
        valor_string = valor_string.replace(","," ");
        Float valor_transacao = Float.parseFloat(valor_string);

        String linkPagamentp = pagamentoClient.post()
                .uri("/gera-transacao")
                .bodyValue(valor_transacao)                     // o objeto será convertido para JSON automaticamente
                .retrieve()
                .bodyToMono(String.class)           // resposta esperada
                .block();

        int transacao = Integer.parseInt(linkPagamentp.split("\"trasacao\":")[1].split(",")[0]);

        if(!transacoes.containsKey(username)){
            transacoes.put(username, new ArrayList<Integer>());
        }

        transacoes.get(username).add(transacao);

        usuarioAssociadoTransacao.put(transacao, username);

        return linkPagamentp;
    }

    public ReservaDTO[] getReservasByUser(String username){
        List<Integer> reservas_indexes = reservas_by_user.get(username);
        List<ReservaDTO> results = new ArrayList<>();

        if(!reservas_by_user.containsKey(username))
            return new ReservaDTO[0];

        if(reservas_indexes.size() == 0)
            return new ReservaDTO[0];

        reservas_indexes.forEach(i -> {
            results.add(reservas.get(i));
        });

        return results.toArray(new ReservaDTO[0]);
    }

    public void cancelaReserva(String username, int index_reserva){
        int real_index = reservas_by_user.get(username).get(index_reserva);
        ReservaDTO reserva = reservas.remove(real_index);

        reservas_by_user.get(username).remove(index_reserva);

        try {
            publicaEmReservaCriada(reserva.serialize());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SseEmitter getSseEmitter(String username){
        if(!sseEmitterManager.containsKey(username)){
            sseEmitterManager.put(username, new SseEmitter(0L));

            sseEmitterManager.get(username).onCompletion(() -> sseEmitterManager.remove(username));
            sseEmitterManager.get(username).onTimeout(() -> sseEmitterManager.remove(username));
            sseEmitterManager.get(username).onError((e) -> sseEmitterManager.remove(username));
        }

        return sseEmitterManager.get(username);
    }

    private void notifyClient(String event, String data, String clientUsername){
        SseEmitter emitter = sseEmitterManager.get(clientUsername);

        if(emitter != null){
            try {
                emitter.send(SseEmitter.event()
                        .name(event)
                        .data(data));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void processaMensagemPagamento(String message, boolean success){
        int transacao = Integer.parseInt(message);
        String username = usuarioAssociadoTransacao.get(transacao);

        if(success){
            notifyClient("pagamento-aprovado", message, username);
        }else{
            notifyClient("pagamento-recusado", message, username);
        }
    }

    private void publicaEmReservaCriada(byte[] message){
        try(Channel channel = connection.createChannel()){

            channel.exchangeDeclare(RESERVAS_EXCHANGE_NAME, "direct");

            channel.basicPublish(
                    RESERVAS_EXCHANGE_NAME,
                    RESERVA_CRIADA_RK,
                    null,
                    message
            );

        } catch (TimeoutException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void publicaEmReservaCancelada(byte[] message){
        try(Channel channel = connection.createChannel()){

            channel.exchangeDeclare(RESERVAS_EXCHANGE_NAME, "direct");

            channel.basicPublish(
                    RESERVAS_EXCHANGE_NAME,
                    RESERVA_CANCELADA_RK,
                    null,
                    message
            );

        } catch (TimeoutException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
