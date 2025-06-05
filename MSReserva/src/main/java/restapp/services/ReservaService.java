package restapp.services;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import mock.ItinerarioMock;
import restapp.DTOs.ConsultaItinerariosDTO;
import restapp.DTOs.EfetuarReservadDTO;
import restapp.DTOs.ItinerarioDTO;
import restapp.utils.GetKeyFromFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.*;

public class ReservaService {
    private static final String HOST = "localhost";

    // EXCHANGE NAMES
    private static final String RESERVAS_EXCHANGE_NAME = "reservas";
    private static final String PAGAMENTOS_EXCHANGE_NAME = "pagamentos";
    private static final String BILHETES_EXCHANGE_NAME = "bilhetes";

    // ROUNTING KEYS
    private static final String RESERVA_CRIADA_RK = "reserva_criada";
    private static final String PAGAMENTO_RECUSADO_RK = "pagamento_recusado";
    private static final String PAGAMENTO_APROVADO_RK = "pagamento_aprovado";
    private static final String BILHETE_GERADO_RK = "bilhete_gerado";

    private static final PublicKey pagamentosPublicKey = GetKeyFromFile.loadPublicKey("pagamento.pub");

    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;

    public ReservaService() throws IOException {
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
        List<ItinerarioDTO> itinerarios = ItinerarioMock.objects;
        List<ItinerarioDTO> result = new ArrayList<>();

        for(int i = 0 ; i < itinerarios.size() ; i++){
            ItinerarioDTO itinerario = itinerarios.get(i);
            boolean validacaoPartida = false;
            boolean validacaoPorto = false;
            boolean validacaoDestino = false;

            if(itinerario.porto_de_desembarque.equals(
                         consultaItinerariosDTO.destino)){
                validacaoDestino = true;
            }

            if(itinerario.porto_de_embarque.equals(
                    consultaItinerariosDTO.porto_de_embarque)){
                validacaoPorto = true;
            }

            for(int j = 0 ; j < itinerario.datas_de_partida_disponiveis.length ; i++){
                String data = itinerario.datas_de_partida_disponiveis[j];

                if(consultaItinerariosDTO.data_de_embarque.equals(data)){
                    validacaoPartida = true;
                }
            }

            if(validacaoDestino && validacaoPartida && validacaoPorto){
                result.add(itinerario);
            }
        }


        return result.toArray(new ItinerarioDTO[0]);
    }

    public String efetuaReserva(EfetuarReservadDTO efetuarReservadDTO){
        return null;
    }

    public void cancelaReserva(String reserva){

    }

    private static void processaMensagemPagamento(String message, boolean success){
        System.out.println("** Mensagem recebida de pagamentos: " + message);

        Map<String, String> desMsg = desserializePagamento(message);
        byte [] signatureBytes = Base64.getDecoder().decode(desMsg.get("signature"));

        if(validaChaves(pagamentosPublicKey, signatureBytes, desMsg.get("message"))){
            System.out.println("** Assinatura da mensagem passou pela validação");

            if(!success){
                System.out.println("** Pagamento Falho, Cancelando Reserva");
            }
        }
        else {
            System.out.println("** Assinatura da mensagem não passou pela validação.");
        }
    }

    private static boolean validaChaves(PublicKey publicKey, byte[] signature, String message){

        try {
            Signature verifier = Signature.getInstance("SHA256withRSA");
            verifier.initVerify(publicKey);
            verifier.update(message.getBytes(StandardCharsets.UTF_8));
            return verifier.verify(signature);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<String, String> desserializePagamento(String message){
        HashMap<String, String> desMsg = new HashMap<>();
        String[] fields = message.split(",");

        for(String field : fields){
            String[] splits = field.split(":");
            String key = splits[0];
            String val = splits[1];

            desMsg.put(key, val);
        }

        return desMsg;
    }
}
