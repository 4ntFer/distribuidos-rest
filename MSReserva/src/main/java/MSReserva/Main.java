package MSReserva;

import org.springframework.boot.SpringApplication;
import restapp.RestApp;
import restapp.services.ReservaService;

public class Main {
    public static ReservaService reservaService;

    public static void main(String [] args) throws Exception{
        reservaService = new ReservaService();
        SpringApplication.run(RestApp.class, args);
    }
}
