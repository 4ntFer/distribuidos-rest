package MSPagamentos;


import MSPagamentos.api.PagamentosApiApplication;
import MSPagamentos.api.services.PagamentoService;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Main {
    public static PagamentoService pagamentoService;

    public static void main(String[] args) throws IOException, TimeoutException {
        pagamentoService = new PagamentoService();

        Map<String, Object> props = new HashMap<>();
        props.put("server.port", 8082);

        new SpringApplicationBuilder(PagamentosApiApplication.class)
                .properties(props)
                .run(args);
    }
}
