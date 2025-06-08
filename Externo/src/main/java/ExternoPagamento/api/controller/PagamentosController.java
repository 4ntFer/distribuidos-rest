package ExternoPagamento.api.controller;



import ExternoPagamento.Main;
import ExternoPagamento.api.DTOs.TransacaoDTO;
import ExternoPagamento.api.DTOs.TransacaoGeradaDTO;
import ExternoPagamento.api.services.PagamentoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/processa-pagamentos")
public class PagamentosController {
    private final PagamentoService pagamentoService = Main.pagamentoService;

    @PostMapping("/link")
    public TransacaoGeradaDTO getLinkTransacao(
            @RequestBody Float valor
    ){
        TransacaoDTO transacao = pagamentoService.criaTransacao(valor);
        TransacaoGeradaDTO transacaoGeradaDTO = pagamentoService.criaLinkParaTransacao(transacao);

        return transacaoGeradaDTO;
    }

    @PostMapping("/{id}")
    public TransacaoDTO processa(
            @PathVariable Integer id, @RequestBody Boolean flag
    ){
        return pagamentoService.notificaTransacao(id, flag);
    }
}
