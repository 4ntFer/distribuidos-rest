package MSItinerarios;

import MSItinerarios.api.ItinerariosApiApplication;
import MSItinerarios.api.services.ItinerariosService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Main {
    public static ItinerariosService itinerariosService;

    public static void main(String[] args) throws IOException, TimeoutException {
        itinerariosService = new ItinerariosService();

        Map<String, Object> props = new HashMap<>();
        props.put("server.port", 8081);

        new SpringApplicationBuilder(ItinerariosApiApplication.class)
                .properties(props)
                .run(args);
    }
}
