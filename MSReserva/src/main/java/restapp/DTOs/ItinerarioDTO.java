package restapp.DTOs;

public class ItinerarioDTO {
    public int id;
    public String[] datas_de_partida_disponiveis;
    public String nome_do_navio;
    public String porto_de_embarque;
    public String porto_de_desembarque;
    public String lugares_visitados;
    public String numero_de_noites;
    public String valor_por_pessoa;

    public ItinerarioDTO(int id, String[] datas_de_partida_disponiveis, String nome_do_navio, String porto_de_embarque, String porto_de_desembarque, String lugares_visitados, String numero_de_noites, String valor_por_pessoa) {
        this.id = id;
        this.datas_de_partida_disponiveis = datas_de_partida_disponiveis;
        this.nome_do_navio = nome_do_navio;
        this.porto_de_embarque = porto_de_embarque;
        this.porto_de_desembarque = porto_de_desembarque;
        this.lugares_visitados = lugares_visitados;
        this.numero_de_noites = numero_de_noites;
        this.valor_por_pessoa = valor_por_pessoa;
    }
}
