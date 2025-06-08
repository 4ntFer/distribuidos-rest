package MSPagamentos.api.DTOs;

public class TransacaoGeradaDTO {
    public int trasacao;
    public String link;

    public TransacaoGeradaDTO(int trasacao, String link) {
        this.trasacao = trasacao;
        this.link = link;
    }
}
