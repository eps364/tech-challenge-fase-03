
package br.com.fiap.orchestrator.infra.web.controller;

import br.com.fiap.orchestrator.core.dto.RequestEvent;
import br.com.fiap.orchestrator.core.usecase.enviar_para_clientes.EnviarParaClientesUseCase;
import br.com.fiap.orchestrator.core.usecase.enviar_para_pedidos.EnviarParaPedidosUseCase;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orchestrator")
public class RequestsController {

    private final EnviarParaClientesUseCase enviarParaClientesUseCase;

    public RequestsController(EnviarParaClientesUseCase enviarParaClientesUseCase) {
        this.enviarParaClientesUseCase = enviarParaClientesUseCase;
    }

    @PostMapping("/requests")
    public void testarPedidos(@RequestBody @Valid RequestEvent event) {

        enviarParaClientesUseCase.execute(event);
    }
}