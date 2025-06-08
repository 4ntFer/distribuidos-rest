package restapp.controller;

import MSReserva.Main;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import restapp.services.ReservaService;

@RestController
@RequestMapping("/sse")
public class SseController {
    private final ReservaService reservaService = Main.reservaService;

    @GetMapping("/")
    public SseEmitter stream(@RequestParam String username){
        return reservaService.getSseEmitter(username);
    }
}
