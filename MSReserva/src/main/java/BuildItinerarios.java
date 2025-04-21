public class BuildItinerarios {
    public static Itinerario [] getItinerarios(){
        Itinerario[] itinerarios = new Itinerario[10];

        itinerarios[0] = new Itinerario(
                new String[]{"15/05/2025", "22/05/2025", "29/05/2025"},
                "Navio Atlântico",
                "Porto de Santos",
                "Lisboa, Portugal",
                "Lisboa, Porto, Funchal",
                "10",
                "R$ 8.500,00"
        );

        itinerarios[1] = new Itinerario(
                new String[]{"05/06/2025", "12/06/2025"},
                "Estrela do Sul",
                "Porto do Rio de Janeiro",
                "Buenos Aires, Argentina",
                "Montevidéu, Punta del Este, Buenos Aires",
                "7",
                "R$ 5.200,00"
        );

        itinerarios[2] = new Itinerario(
                new String[]{"01/07/2025", "15/07/2025", "29/07/2025"},
                "Mar do Norte",
                "Porto de Salvador",
                "Miami, Estados Unidos",
                "Recife, Barbados, Miami",
                "12",
                "R$ 11.300,00"
        );

        itinerarios[3] = new Itinerario(
                new String[]{"10/06/2025", "24/06/2025"},
                "Sol do Atlântico",
                "Porto de Recife",
                "Cidade do Cabo, África do Sul",
                "Recife, Ilha de Ascensão, Cidade do Cabo",
                "14",
                "R$ 9.700,00"
        );

        itinerarios[4] = new Itinerario(
                new String[]{"18/06/2025", "25/06/2025"},
                "Oceano Azul",
                "Porto de Fortaleza",
                "Barcelona, Espanha",
                "Fortaleza, Ilhas Canárias, Barcelona",
                "11",
                "R$ 10.800,00"
        );

        itinerarios[5] = new Itinerario(
                new String[]{"20/06/2025", "27/06/2025"},
                "Aurora",
                "Porto de Vitória",
                "Gênova, Itália",
                "Vitória, Málaga, Roma, Gênova",
                "13",
                "R$ 12.200,00"
        );

        itinerarios[6] = new Itinerario(
                new String[]{"25/06/2025", "02/07/2025"},
                "Caravela",
                "Porto de Belém",
                "Casablanca, Marrocos",
                "Belém, Dakar, Casablanca",
                "9",
                "R$ 6.950,00"
        );

        itinerarios[7] = new Itinerario(
                new String[]{"01/07/2025", "08/07/2025"},
                "Atlântida",
                "Porto de Paranaguá",
                "Nova Iorque, Estados Unidos",
                "Paranaguá, Nassau, Nova Iorque",
                "15",
                "R$ 13.900,00"
        );

        itinerarios[8] = new Itinerario(
                new String[]{"10/07/2025", "17/07/2025"},
                "Navegaré",
                "Porto de São Luís",
                "Marselha, França",
                "São Luís, Açores, Lisboa, Marselha",
                "16",
                "R$ 14.300,00"
        );

        itinerarios[9] = new Itinerario(
                new String[]{"20/07/2025", "27/07/2025"},
                "Ventos do Sul",
                "Porto de Maceió",
                "Atenas, Grécia",
                "Maceió, Casablanca, Sicília, Atenas",
                "18",
                "R$ 15.800,00"
        );

        return itinerarios;
    }
}
