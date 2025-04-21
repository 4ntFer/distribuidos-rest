import com.rabbitmq.client.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    private final static String ADV_EXC = "Advertisement";
    private final static String ADV_TOPIC = "location";
    private final static String[] locations = {"Porto de Galinhas","Paranagua","Rio de Janeiro","Belém"};
    private final static String[] codes = {"BR.PE.porto_de_galinhas","BR.PR.paranagua","BR.RJ.rio_de_janeiro","BR.PA.belem"};

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(ADV_EXC, "topic");

        String queue = channel.queueDeclare().getQueue();

        DeliverCallback deliverCallback = (_, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("--> Oferta Recebida: " + message);
        };
        channel.basicConsume(queue, true, deliverCallback, _ -> {});

        System.out.println("** Escolha quais anúncios escutar:");
        for(int i = 0; i<locations.length;i++)
        {
            System.out.println("["+i+"] " + locations[i]);
        }
        boolean flag_run = true;
        while(flag_run) {
            Scanner scanner = new Scanner(System.in);
            int id = scanner.nextInt();
            if(id<0 || id>= locations.length){
                flag_run = false;
            } else {
                channel.queueBind(queue, ADV_EXC, ADV_TOPIC + "." + codes[id]);
                System.out.println("** ANÚNCIOS DE " + ADV_EXC + "(topic: " + ADV_TOPIC + "." + codes[id] + ") SERÃO ESCUTADOS");
            }
        }

    }
}
