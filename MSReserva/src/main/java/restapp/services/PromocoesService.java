package restapp.services;

import MSReserva.Main;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeoutException;

public class PromocoesService {
    private final static String ADV_EXC = "Advertisement";

    private final SseService sseService = Main.sseService;

    private final Set<String> interessados = new HashSet<>();

    public PromocoesService() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(ADV_EXC, "fanout");

        String queue = channel.queueDeclare().getQueue();
        channel.queueBind(queue, ADV_EXC, "");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(message);

            notify(message);
        };
        channel.basicConsume(queue, true, deliverCallback, consumerTag -> {});

    }

    public void registrarInteresse(String username){
        interessados.add(username);
    }

    public void cancelarInteresse(String username){
        interessados.remove(username);
    }

    private void notify(String promocao){
        interessados.forEach(user -> {
            SseEmitter emitter = sseService.getSseEmitter(user);

            if(emitter != null){
                try {
                    emitter.send(SseEmitter.event()
                            .name("promocao")
                            .data(promocao));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
