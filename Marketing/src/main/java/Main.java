import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    private final static String ADV_X_EXC = "AdvertiseX";
    private final static String ADV_Y_EXC = "AdvertiseY";

    public static void main(String[] args) throws Exception {
        //START RABBIT MQ QUEUES
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        //GET PRIVATE KEY TODO
        try(Connection connection = factory.newConnection();
            Channel channel = connection.createChannel()){

            channel.exchangeDeclare(ADV_X_EXC,"fanout");
            channel.exchangeDeclare(ADV_Y_EXC,"fanout");
            int info;
            //LOOP TO SEND TO MARKETING

            do {
                String message;
                String exchange;

                System.out.println("X to advertise to X cruise, Y to advertise to Y cruise, N to stop process.");
                info = System.in.read();

                switch (info) {
                    case 'X':
                        exchange = ADV_X_EXC;
                        message = "Cruise X Offer "+ThreadLocalRandom.current().nextInt(0, 101)+"% off - " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                        break;
                    case 'Y':
                        exchange = ADV_Y_EXC;
                        message = "Cruise Y Offer "+ThreadLocalRandom.current().nextInt(0, 101)+"% off - " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                        break;
                    default:
                        continue;

                }
                channel.basicPublish(exchange,"",null,message.getBytes(StandardCharsets.UTF_8));
                System.out.println("SENT TO " + exchange + " MESSAGE:\"" + message+"\"");
            } while (info!= 'N');
        }
    }
}
