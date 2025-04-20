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

public class MSBilhete {

    private static final String PAGAMENTOS_EXCHANGE_NAME = "pagamentos";
    private static final String BILHETES_EXCHANGE_NAME = "bilhetes";

    private static final String PAGAMENTO_APROVADO_RK = "pagamento_aprovado";
    private static final String BILHETE_GERADO_RK = "bilhete_gerado";
    private static final PublicKey publicKey = GetKeyFromFile.loadPublicKey("pagamento.pub");

    private static Connection connection;

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        connection = factory.newConnection();

        Channel channel = connection.createChannel();

        String pagamento_aprovado_queue_name = channel.queueDeclare().getQueue();

        channel.exchangeDeclare(PAGAMENTOS_EXCHANGE_NAME, "direct");
        channel.queueBind(
                pagamento_aprovado_queue_name, PAGAMENTOS_EXCHANGE_NAME, PAGAMENTO_APROVADO_RK
        );
        channel.exchangeDeclare(BILHETES_EXCHANGE_NAME, "direct");
        DeliverCallback PagamentoCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            try {
                processaMensagemBilhete(channel, message);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
        channel.basicConsume(pagamento_aprovado_queue_name,true, PagamentoCallback, consumerTag -> {});
    }

    private static void processaMensagemBilhete(Channel channel, String message) throws Exception{
        System.out.println("** Mensagem recebida de pagamentos: " + message);

        Map<String, String> desMsg = desserializePagamento(message);
        byte [] signatureBytes = Base64.getDecoder().decode(desMsg.get("signature"));

        if(validaChaves(publicKey, signatureBytes, desMsg.get("message"))){
            System.out.println("** Assinatura da mensagem passou pela validação");
            String out_message = desMsg.get("message") +" | BILHETE GERADO, NUM XXXX";
            System.out.println("** Enviando BILHETE: " + out_message);

            channel.basicPublish(BILHETES_EXCHANGE_NAME,
                    BILHETE_GERADO_RK,
                    null,
                    out_message.getBytes());

        } else {
            System.out.println("** Assinatura da mensagem não passou pela validação");
            System.out.println("** Invalidando Mensagem");

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