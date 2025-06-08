package MSPagamentos.api.controller;

import MSPagamentos.Main;
import MSPagamentos.api.DTOs.TransacaoDTO;
import MSPagamentos.api.DTOs.TransacaoGeradaDTO;
import MSPagamentos.api.services.PagamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagamentos/")
public class PagamentosController {
    private final PagamentoService pagamentoService = Main.pagamentoService;

    @PostMapping("/gera-transacao")
    public TransacaoGeradaDTO getLinkTransacao(
            @RequestBody Float valor
    ){
        return pagamentoService.criaTransacao(valor);
    }

    @PostMapping("/notifica-transacao")
    public ResponseEntity<String> notificaTransacao(
            @RequestBody TransacaoDTO transacaoDTO
    ){
        // TODO:

        return ResponseEntity.ok("");
    }
}
