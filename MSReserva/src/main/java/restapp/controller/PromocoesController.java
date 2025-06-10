package restapp.controller;

import MSReserva.Main;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import restapp.services.PromocoesService;

@RestController
@RequestMapping("/api/promocoes")
public class PromocoesController {
    private final PromocoesService promocoesService = Main.promocoesService;

    @GetMapping("/registrar-interesse")
    public ResponseEntity<String> registrarInteresse(
            @RequestHeader String username
    ){
        promocoesService.registrarInteresse(username);
        return ResponseEntity.ok("");
    }

    @GetMapping("/cancelar-interesse")
    public ResponseEntity<String> cancelarInteresse(
            @RequestHeader String username
    ){
        promocoesService.cancelarInteresse(username);
        return ResponseEntity.ok("");
    }
}
