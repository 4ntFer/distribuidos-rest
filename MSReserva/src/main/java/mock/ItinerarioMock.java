package mock;

import restapp.DTOs.ItinerarioDTO;

import java.util.ArrayList;
import java.util.List;

public class ItinerarioMock {
    public static final List<ItinerarioDTO> objects = ItinerarioMock.gerarMock();

    public static List<ItinerarioDTO> gerarMock() {
        List<ItinerarioDTO> lista = new ArrayList<>();

        String[][] datas = {
                {"2025-06-01", "2025-06-15"},
                {"2025-07-03", "2025-07-17"},
                {"2025-08-05", "2025-08-19"},
                {"2025-09-07", "2025-09-21"},
                {"2025-10-09", "2025-10-23"},
                {"2025-11-11", "2025-11-25"},
                {"2025-12-13", "2025-12-27"},
                {"2025-01-15", "2025-01-29"},
                {"2025-02-17", "2025-03-03"},
                {"2025-03-19", "2025-04-02"},
                {"2025-04-21", "2025-05-05"},
                {"2025-05-23", "2025-06-06"},
                {"2025-06-25", "2025-07-09"},
                {"2025-07-27", "2025-08-10"},
                {"2025-08-29", "2025-09-12"},
                {"2025-09-30", "2025-10-14"},
                {"2025-11-01", "2025-11-15"},
                {"2025-12-03", "2025-12-17"},
                {"2025-01-05", "2025-01-19"},
                {"2025-02-07", "2025-02-21"}
        };

        String[] navios = {
                "MSC Fantasia", "Costa Diadema", "Norwegian Sun", "Royal Princess", "Celebrity Edge",
                "Harmony of the Seas", "Azamara Quest", "MSC Preziosa", "Costa Favolosa", "Oasis of the Seas",
                "Allure of the Seas", "MSC Seaside", "MSC Splendida", "Costa Toscana", "Norwegian Epic",
                "MSC Meraviglia", "Seabourn Encore", "AIDAnova", "MSC Opera", "Costa Pacifica"
        };

        String[] portosEmbarque = {
                "Santos", "Rio de Janeiro", "Fortaleza", "Salvador", "Recife",
                "Buenos Aires", "Barcelona", "Lisboa", "Miami", "Gênova",
                "Marselha", "Veneza", "Valência", "Hamburgo", "Copenhague",
                "Atenas", "Dubrovnik", "Palermo", "Istambul", "Nápoles"
        };

        String[] portosDesembarque = {
                "Salvador", "Buenos Aires", "Recife", "Santos", "Rio de Janeiro",
                "Fortaleza", "Barcelona", "Miami", "Lisboa", "Gênova",
                "Marselha", "Veneza", "Valência", "Hamburgo", "Copenhague",
                "Atenas", "Istambul", "Palermo", "Dubrovnik", "Nápoles"
        };

        String[] lugares = {
                "Búzios, Ilhéus, Salvador", "Montevidéu, Punta del Este", "Ilha Bela, Rio", "Natal, João Pessoa, Recife",
                "Barcelona, Valência, Marselha", "Santos, Angra, Ilhéus", "Salvador, Maceió, Recife",
                "Rio, Salvador, Buenos Aires", "Gênova, Barcelona, Lisboa", "Veneza, Dubrovnik, Atenas",
                "Valência, Cagliari, Nápoles", "Miami, Nassau, Cozumel", "Marselha, Roma, Gênova",
                "Dubrovnik, Santorini, Mykonos", "Fortaleza, Belém, Manaus",
                "Recife, Fernando de Noronha, Natal", "Bali, Singapura, Phuket",
                "Rio, Montevidéu, Punta", "Buenos Aires, Ilhéus, Recife", "Rio, Búzios, Salvador"
        };

        String[] noites = {
                "7", "9", "5", "12", "10",
                "8", "6", "11", "14", "7",
                "4", "13", "15", "10", "6",
                "9", "8", "12", "7", "5"
        };

        String[] valores = {
                "R$ 3.499", "R$ 4.799", "R$ 2.199", "R$ 5.299", "R$ 3.999",
                "R$ 3.899", "R$ 2.999", "R$ 6.199", "R$ 7.499", "R$ 4.199",
                "R$ 1.999", "R$ 5.499", "R$ 4.899", "R$ 5.999", "R$ 3.599",
                "R$ 2.499", "R$ 6.999", "R$ 3.799", "R$ 4.299", "R$ 2.799"
        };

        for (int i = 0; i < 20; i++) {
            ItinerarioDTO itinerario = new ItinerarioDTO(
                    i,
                    datas[i],
                    navios[i],
                    portosEmbarque[i],
                    portosDesembarque[i],
                    lugares[i],
                    noites[i],
                    valores[i]
            );
            lista.add(itinerario);
        }

        return lista;
    }
}
