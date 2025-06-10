package MSReserva;

import org.springframework.boot.SpringApplication;
import restapp.RestApp;
import restapp.services.PromocoesService;
import restapp.services.ReservaService;
import restapp.services.SseService;

public class Main {
    public static ReservaService reservaService;
    public static PromocoesService promocoesService;
    public static SseService sseService;

    public static void main(String [] args) throws Exception{
        sseService = new SseService();
        promocoesService = new PromocoesService();
        reservaService = new ReservaService();
        SpringApplication.run(RestApp.class, args);
    }
}
