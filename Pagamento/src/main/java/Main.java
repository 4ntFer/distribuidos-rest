import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

import com.rabbitmq.client.*;
import java.nio.charset.StandardCharsets;

import java.security.PrivateKey;
import java.security.*;
import java.util.Base64;
import java.util.concurrent.ThreadLocalRandom;



public class Main {
    private final static String PAYMENT_EXCHANGE = "payment_exchange";
    private final static String RESERVE_QUEUE = "reserva_criada";
    private static PrivateKey privKey;


    public static void processPayment(Channel channel,String message) throws Exception
    {
        String topic;
        //has a 50% chance of failing
        if(ThreadLocalRandom.current().nextInt(1, 11)>5)
        {
            message = message + " || PAYMENT SUCCESSFUL";
            topic = "success";
        } else
        {
            message = message + " || PAYMENT FAILED";
            topic = "fail";
        }


        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privKey);
        signature.update(message.getBytes(StandardCharsets.UTF_8));
        String signed_message = message+"\n----- SIGNATURE -----\n"+ Base64.getEncoder().encodeToString(signature.sign());
        System.out.println("RESULT OF PAYMENT: " +signed_message);
        channel.basicPublish(PAYMENT_EXCHANGE,"payment."+topic,null,signed_message.getBytes(StandardCharsets.UTF_8));
    }

    public static void main(String[] args) throws Exception {
        privKey =  GetKeyFromFile.loadPrivateKey("pagamento.priv");
        //START RABBIT MQ QUEUES
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try(Connection connection = factory.newConnection();
            Channel channel = connection.createChannel()){
            channel.exchangeDeclare(PAYMENT_EXCHANGE,"topic");
            channel.queueDeclare(RESERVE_QUEUE, true, false, false, null);

            DeliverCallback callback = (_, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println("Reserva Recebida: " + message);

                try {
                    processPayment(channel, message);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            };
            channel.basicConsume(RESERVE_QUEUE, true, callback, _ -> {});
            System.out.println("MS Pagamento started. ANY KEY TO STOP.");
            System.in.read();

        }
    }
}
