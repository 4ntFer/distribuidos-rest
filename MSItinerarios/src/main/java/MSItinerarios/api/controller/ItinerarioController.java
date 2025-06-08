package MSItinerarios.api.controller;

import MSItinerarios.Main;

import MSItinerarios.api.DTOs.ConsultaItinerariosDTO;
import MSItinerarios.api.DTOs.ItinerarioDTO;
import MSItinerarios.api.services.ItinerariosService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/itinerarios")
public class ItinerarioController {
    private static final ItinerariosService itinerarioService = Main.itinerariosService;

    @PostMapping("/consulta")
    public ResponseEntity<ItinerarioDTO[]> consultarItineararios(
            @RequestBody ConsultaItinerariosDTO consultaItinerariosDTO) {

        ItinerarioDTO[] result = itinerarioService.consultaItinerarios(consultaItinerariosDTO);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/consulta")
    public ResponseEntity<ItinerarioDTO[]> consultarItineararios() {

        ItinerarioDTO[] result = itinerarioService.consultaItinerarios();

        return ResponseEntity.ok(result);
    }
}
