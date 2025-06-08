package MSPagamentos.api.services;

import MSPagamentos.api.DTOs.TransacaoDTO;
import MSPagamentos.api.DTOs.TransacaoGeradaDTO;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class PagamentoService {
    private static final String PAGAMENTO_RECUSADO_RK = "pagamento_recusado";
    private static final String PAGAMENTO_APROVADO_RK = "pagamento_aprovado";

    private static final String PAGAMETOS_EXCHANGE_NAME = "pagamentos";

    private Connection connection;
    private ConnectionFactory factory;

    private Map<Integer, TransacaoDTO> transacoes = new HashMap<>();
    private int max_id = 0;

    private WebClient externo = WebClient.builder().baseUrl("http://localhost:8083/api/processa-pagamentos").build();

   public PagamentoService() throws IOException, TimeoutException {
       ConnectionFactory factory = new ConnectionFactory();
       connection = factory.newConnection();
   }

    public TransacaoGeradaDTO criaTransacao(float valor){
        return externo.post()
                .uri("/link")
                .bodyValue(valor)
                .retrieve()
                .bodyToMono(TransacaoGeradaDTO.class)
                .block();
    }

    public void processaNotificacaoDeTransacao(TransacaoDTO transacaoDTO){
        if(transacaoDTO.status == 1){
            publicaEmPagamentoAprovado(""+transacaoDTO.id);
        }

        if(transacaoDTO.status == 2){
            publicaEmPagamentoRecusado(""+transacaoDTO.id);
        }
    }

    private void publicaEmPagamentoRecusado(String message){
        try(Channel channel = connection.createChannel()){

            channel.exchangeDeclare(PAGAMETOS_EXCHANGE_NAME, "direct");

            channel.basicPublish(
                    PAGAMETOS_EXCHANGE_NAME,
                    PAGAMENTO_RECUSADO_RK,
                    null,
                    message.getBytes()
            );

        } catch (TimeoutException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void publicaEmPagamentoAprovado(String message) {
        try(Channel channel = connection.createChannel()){

            channel.exchangeDeclare(PAGAMETOS_EXCHANGE_NAME, "direct");

            channel.basicPublish(
                    PAGAMETOS_EXCHANGE_NAME,
                    PAGAMENTO_APROVADO_RK,
                    null,
                    message.getBytes()
            );

        } catch (TimeoutException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
