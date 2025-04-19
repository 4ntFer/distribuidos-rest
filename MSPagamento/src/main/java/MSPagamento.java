import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MSPagamento {
    private static final String HOST = "localhost";
    private static final String RESERVA_CRIADA_RK = "reserva_criada";
    private static final String PAGAMENTO_RECUSADO_RK = "pagamento_recusado";
    private static final String PAGAMENTO_APROVADO_RK = "pagamento_aprovado";

    private static final String RESERVAS_EXCHANGE_NAME = "reservas";
    private static final String PAGAMETOS_EXCHANGE_NAME = "pagamentos";

    private static Connection connection;

    public static void main(String [] args) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        connection = factory.newConnection();

        Channel channel = connection.createChannel();

        String reservas_criadas_queue_name = channel.queueDeclare().getQueue();

        channel.exchangeDeclare(RESERVAS_EXCHANGE_NAME, "direct");
        channel.queueBind(
                reservas_criadas_queue_name,
                RESERVAS_EXCHANGE_NAME,
                RESERVA_CRIADA_RK
        );

        DeliverCallback reservaCriadaCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(message);

            if(validaPagamento()){
                publicaEmPagamentoAprovado("Pagamento Aprovado");
            }else {
                publicaEmPagamentoRecusado("Pagamento Recusado");
            }
        };

        channel.basicConsume(reservas_criadas_queue_name, true, reservaCriadaCallback, cosumerTag -> {});
    }

    private static boolean validaPagamento(){
        return true;
    }

    private static void publicaEmPagamentoAprovado(String message) {
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

    private static void publicaEmPagamentoRecusado(String message){
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
}
