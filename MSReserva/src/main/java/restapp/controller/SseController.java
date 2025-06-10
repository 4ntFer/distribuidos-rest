package restapp.controller;

import MSReserva.Main;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import restapp.services.ReservaService;
import restapp.services.SseService;

@RestController
@RequestMapping("/sse")
public class SseController {
    private final SseService sseService = Main.sseService;

    @GetMapping("/")
    public SseEmitter stream(@RequestParam String username){
        return sseService.getSseEmitter(username);
    }
}
