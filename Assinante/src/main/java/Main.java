import com.rabbitmq.client.*;

import java.nio.charset.StandardCharsets;

public class Main {
    private final static String ADV_X_EXC = "AdvertiseX";
    private final static String ADV_Y_EXC = "AdvertiseY";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try(Connection connection = factory.newConnection();
            Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(ADV_X_EXC, "fanout");
            channel.exchangeDeclare(ADV_Y_EXC, "fanout");
            System.out.println("Write X to receive offers from X, Y to receive offers from Y, A to both");
            int character = System.in.read();
            String queue = channel.queueDeclare().getQueue();

            switch (character)
            {
                case 'X':
                    channel.queueBind(queue, ADV_X_EXC, "");
                    break;
                case 'Y':
                    channel.queueBind(queue, ADV_Y_EXC, "");
                    break;
                default:
                    channel.queueBind(queue, ADV_X_EXC, "");
                    channel.queueBind(queue, ADV_Y_EXC, "");
            }



            DeliverCallback deliverCallback = (_, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println("Offer Received: " + message);
            };
            channel.basicConsume(queue, true, deliverCallback, _ -> {});
            while(System.in.available()>0)
                System.in.read();
            System.in.read();
        }
    }
}
