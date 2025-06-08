package MSPagamentos.api.DTOs;

public class TransacaoDTO {
    public int id;
    public int status; // 0 pendente, 1 autorizado, 2 recusado
    public float valor;
    public String comprador;

    public TransacaoDTO(int id, int status, float valor, String comprador) {
        this.id = id;
        this.status = status;
        this.valor = valor;
        this.comprador = comprador;
    }
}
