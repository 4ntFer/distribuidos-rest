import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class MSReserva {
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

    private static Connection connection;

    public static void main(String [] args) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        connection = factory.newConnection();

        Channel channel = connection.createChannel();

        // ESCUTA

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

        HandleClient client = new HandleClient();

        client.criaReservaDoCliente();

        System.out.println("** Criando reserva");
        System.out.println("** Publicando em reserva criada");

        if(client.reserva != null){
            System.out.println("** Criando reserva");
            System.out.println("** Publicando em reserva criada");
            publicaEmReservaCriada(
                    client.reserva.toString()
                            .replace(":","=")
                            .replace(",", ".")
            );
        }
        else{
            System.out.println("** Cancelando criação de reserva");
        }
    }

    private static void publicaEmReservaCriada(String message){
        try(Channel channel = connection.createChannel()){

            channel.exchangeDeclare(RESERVAS_EXCHANGE_NAME, "direct");

            channel.basicPublish(
                    RESERVAS_EXCHANGE_NAME,
                    RESERVA_CRIADA_RK,
                    null,
                    message.getBytes()
            );

        } catch (TimeoutException | IOException e) {
            throw new RuntimeException(e);
        }
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
