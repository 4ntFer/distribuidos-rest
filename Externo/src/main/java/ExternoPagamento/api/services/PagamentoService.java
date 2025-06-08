package ExternoPagamento.api.services;


import ExternoPagamento.api.DTOs.TransacaoDTO;
import ExternoPagamento.api.DTOs.TransacaoGeradaDTO;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

public class PagamentoService {
    private Map<Integer, TransacaoDTO> transacoes = new HashMap<>();
    private int max_id = 0;

    private WebClient pagamentoClient = WebClient.builder().baseUrl("http://localhost:8082/api/pagamentos").build();

    public TransacaoDTO criaTransacao(float valor){
        TransacaoDTO transacaoDTO = new TransacaoDTO(
                max_id,
                0,
                valor,
                null
        );

        transacoes.put(max_id,transacaoDTO);

        max_id++;
        return transacaoDTO;
    }

    public TransacaoGeradaDTO criaLinkParaTransacao(TransacaoDTO transacaoDto){
        return new TransacaoGeradaDTO(
                transacaoDto.id,
                "http://localhost:8083/api/processa-pagamentos/" + transacaoDto.id);
    }

    public TransacaoDTO notificaTransacao(int id, boolean flag){
        TransacaoDTO transacao = transacoes.get(id);

        if(flag){
            transacao.status = 1;
        }
        else{
            transacao.status = 2;
        }

        pagamentoClient.post()
                .uri("/notifica-transacao")
                .bodyValue(transacao)
                .retrieve()
                .toBodilessEntity()
                .subscribe(); // Ignorando resutado, inclusive erros

        return transacao;
    }
}
