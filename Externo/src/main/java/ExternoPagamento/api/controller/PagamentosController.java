package ExternoPagamento.api.controller;



import ExternoPagamento.Main;
import ExternoPagamento.api.DTOs.TransacaoDTO;
import ExternoPagamento.api.DTOs.TransacaoGeradaDTO;
import ExternoPagamento.api.services.PagamentoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/link/{id}")
    public TransacaoDTO getLinkTransacao(
            int id, @RequestBody Boolean flag
    ){
        return pagamentoService.notificaTransacao(id, flag);
    }
}
