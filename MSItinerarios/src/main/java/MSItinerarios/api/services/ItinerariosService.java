package MSItinerarios.api.services;

import MSItinerarios.api.DTOs.ConsultaItinerariosDTO;
import MSItinerarios.api.DTOs.ItinerarioDTO;
import MSItinerarios.api.DTOs.ReservaDTO;
import MSItinerarios.api.services.mock.ItinerarioMock;

import java.util.ArrayList;
import java.util.List;

public class ItinerariosService {
    private static final List<ItinerarioDTO> itinerarios = ItinerarioMock.objects;

    public ItinerarioDTO[] consultaItinerarios(ConsultaItinerariosDTO consultaItinerariosDTO){
        List<ItinerarioDTO> result = new ArrayList<>();

        for(int i = 0 ; i < itinerarios.size() ; i++){
            ItinerarioDTO itinerario = itinerarios.get(i);
            boolean validacaoPartida = false;
            boolean validacaoPorto = false;
            boolean validacaoDestino = false;

            if(itinerario.porto_de_desembarque.equals(
                    consultaItinerariosDTO.destino)){
                validacaoDestino = true;
            }

            if(itinerario.porto_de_embarque.equals(
                    consultaItinerariosDTO.porto_de_embarque)){
                validacaoPorto = true;
            }

            for(int j = 0 ; j < itinerario.datas_de_partida_disponiveis.length ; j++){
                String data = itinerario.datas_de_partida_disponiveis[j];

                if(consultaItinerariosDTO.data_de_embarque.equals(data)){
                    validacaoPartida = true;
                }
            }

            if(validacaoDestino && validacaoPartida && validacaoPorto){
                result.add(itinerario);
            }
        }


        return result.toArray(new ItinerarioDTO[0]);
    }

    public void reservaCriadaHandle(ReservaDTO reservaCriada){
        ItinerarioDTO itinerario = itinerarios.get(reservaCriada.itinerario);
        itinerario.cabines_disponiveis--;
    }

    public ItinerarioDTO[] consultaItinerarios(){
        return itinerarios.toArray(new ItinerarioDTO[0]);
    }

    public void reservaCancelardaHandle(ReservaDTO reservaCancelada){
        ItinerarioDTO itinerario = itinerarios.get(reservaCancelada.itinerario);
        itinerario.cabines_disponiveis++;
    }
}
