public class Itinerario {
    public String[] datas_de_partida_disponiveis;
    public String nome_do_navio;
    public String porto_de_embarque;
    public String porto_de_desembarque;
    public String lugares_visitados;
    public String numero_de_noites;
    public String valor_por_pessoa;

    public Itinerario(
            String[] datas,
            String navio,
            String embarque,
            String desembarque,
            String lugares,
            String noites,
            String valor
    ) {
        this.datas_de_partida_disponiveis = datas;
        this.nome_do_navio = navio;
        this.porto_de_embarque = embarque;
        this.porto_de_desembarque = desembarque;
        this.lugares_visitados = lugares;
        this.numero_de_noites = noites;
        this.valor_por_pessoa = valor;
    }

    public boolean matchesWith(String destino, String data_de_embarque, String porto_de_embarque){
        boolean embarqueFlag = false;


        for(String data : datas_de_partida_disponiveis){
            if(data_de_embarque.equals(data) || data_de_embarque.isEmpty()){
                embarqueFlag = true;

                break;
            }
        }

        return ((destino.equals(porto_de_desembarque)|| destino.isEmpty())&&
                embarqueFlag &&
                (porto_de_embarque.equals(this.porto_de_embarque) || porto_de_embarque.isEmpty()));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Navio: ").append(nome_do_navio).append("\n");
        sb.append("Porto de Embarque: ").append(porto_de_embarque).append("\n");
        sb.append("Porto de Desembarque: ").append(porto_de_desembarque).append("\n");
        sb.append("Lugares Visitados: ").append(lugares_visitados).append("\n");
        sb.append("Número de Noites: ").append(numero_de_noites).append("\n");
        sb.append("Valor por Pessoa: ").append(valor_por_pessoa).append("\n");
        sb.append("Datas de Partida Disponíveis:\n");
        for (String data : datas_de_partida_disponiveis) {
            sb.append("  - ").append(data).append("\n");
        }
        return sb.toString();
    }
}
