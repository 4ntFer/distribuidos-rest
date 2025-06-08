package MSPagamentos.api.services;

import MSPagamentos.api.DTOs.TransacaoDTO;
import MSPagamentos.api.DTOs.TransacaoGeradaDTO;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

public class PagamentoService {
    private Map<Integer, TransacaoDTO> transacoes = new HashMap<>();
    private int max_id = 0;

    private WebClient externo = WebClient.builder().baseUrl("http://localhost:8083/api/processa-pagamentos").build();

    public TransacaoGeradaDTO criaTransacao(float valor){
        return externo.post()
                .uri("/link")
                .bodyValue(valor)
                .retrieve()
                .bodyToMono(TransacaoGeradaDTO.class)
                .block();
    }
}
