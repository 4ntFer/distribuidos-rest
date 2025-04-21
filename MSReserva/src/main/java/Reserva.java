public class Reserva {
    public String data_de_embarque;
    public String numero_de_passageiros;
    public String numero_de_cabines;
    public Itinerario itinerario;

    public Reserva(String data_de_embarque, String numero_de_passageiros, String numero_de_cabines, Itinerario itinerario) {
        this.data_de_embarque = data_de_embarque;
        this.numero_de_passageiros = numero_de_passageiros;
        this.numero_de_cabines = numero_de_cabines;
        this.itinerario = itinerario;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"data_de_embarque\": \"").append(data_de_embarque).append("\",\n");
        sb.append("  \"numero_de_passageiros\": \"").append(numero_de_passageiros).append("\",\n");
        sb.append("  \"numero_de_cabines\": \"").append(numero_de_cabines).append("\",\n");
        sb.append("  \"itinerario\": ").append(itinerarioToJson()).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String itinerarioToJson() {
        if (itinerario == null) {
            return "null";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("    \"nome_do_navio\": \"").append(itinerario.nome_do_navio).append("\",\n");
        sb.append("    \"porto_de_embarque\": \"").append(itinerario.porto_de_embarque).append("\",\n");
        sb.append("    \"porto_de_desembarque\": \"").append(itinerario.porto_de_desembarque).append("\",\n");
        sb.append("    \"lugares_visitados\": \"").append(itinerario.lugares_visitados).append("\",\n");
        sb.append("    \"numero_de_noites\": \"").append(itinerario.numero_de_noites).append("\",\n");
        sb.append("    \"valor_por_pessoa\": \"").append(itinerario.valor_por_pessoa).append("\",\n");
        sb.append("    \"datas_de_partida_disponiveis\": [");

        for (int i = 0; i < itinerario.datas_de_partida_disponiveis.length; i++) {
            sb.append("\"").append(itinerario.datas_de_partida_disponiveis[i]).append("\"");
            if (i < itinerario.datas_de_partida_disponiveis.length - 1) {
                sb.append(", ");
            }
        }

        sb.append("]\n  }");
        return sb.toString();
    }
}
