import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

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
        //START RABBIT MQ QUEUES
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try(Connection connection = factory.newConnection();
            Channel channel = connection.createChannel()){

            channel.exchangeDeclare(ADV_EXC,"topic");
            int info;

            //LOOP TO SEND TO MARKETING
            boolean flag_run = true;
            while(flag_run) {



                System.out.println("Escolha um Lugar para Anunciar:");
                for(int i = 0; i<locations.length;i++)
                {
                    System.out.println("["+i+"]" + locations[i]);
                }

                Scanner scanner = new Scanner(System.in);
                int id = scanner.nextInt();
                if(id<0 || id>= locations.length){
                    flag_run = false;
                } else {
                    String message = "Cruseiro saindo de " + locations[id] + " | Oferta: " + ThreadLocalRandom.current().nextInt(0, 101) + "% off - " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

                    channel.basicPublish(ADV_EXC, ADV_TOPIC + "." + codes[id], null, message.getBytes(StandardCharsets.UTF_8));
                    System.out.println("SENT TO " + ADV_EXC + "(topic: " + ADV_TOPIC + "." + codes[id] + ")" + " MESSAGE:\"" + message + "\"");
                }
            }
        }
    }
}
