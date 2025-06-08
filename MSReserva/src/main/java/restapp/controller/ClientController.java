package restapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClientController {
    @GetMapping("/")
    public String Home(){
        return "home";
    }

    @GetMapping("/itinerarios")
    public String Itinerarios(){
        return "listaItinerarios";
    }

    @GetMapping("/efetuar-reserva")
    public String reserva(){
        return "efetuaReserva";
    }
}
