package restapp.controller;

import MSReserva.Main;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restapp.DTOs.ConsultaItinerariosDTO;
import restapp.DTOs.EfetuarReservadDTO;
import restapp.DTOs.ItinerarioDTO;
import restapp.services.ReservaService;

@RestController
@RequestMapping("/api/reserva")
public class ReservaControler {
    private static final ReservaService reservaService = Main.reservaService;

    @GetMapping("/teste")
    public String teste() {
        return "teste";
    }

    @PostMapping("/consulta-itinerarios")
    public ResponseEntity<ItinerarioDTO[]> consultarItineararios(
            @RequestBody ConsultaItinerariosDTO consultaItinerariosDTO) {

        ItinerarioDTO[] result = reservaService.consultaItinerarios(consultaItinerariosDTO);

        return ResponseEntity.ok(result);
    }

    @PostMapping
    public String efetuarReserva(
            @RequestBody EfetuarReservadDTO efetuarReservadDTO) {
        return null;
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> cancelarReserva(@PathVariable int id) {
        return null;
    }
}
