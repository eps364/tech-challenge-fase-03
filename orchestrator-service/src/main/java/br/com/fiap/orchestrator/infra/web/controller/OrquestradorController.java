
package br.com.fiap.orchestrator.infra.web.controller;

import br.com.fiap.orchestrator.core.dto.RequestEvent;
import br.com.fiap.orchestrator.core.usecase.enviar_para_pedidos.EnviarParaPedidosUseCase;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/orquestrador")
public class OrquestradorController {

    private final EnviarParaPedidosUseCase enviarParaPedidos;

    public OrquestradorController(EnviarParaPedidosUseCase enviarParaPedidos) {
        this.enviarParaPedidos = enviarParaPedidos;
    }

//    @PostMapping("/teste/pedidos")
//    public void testarPedidos(@RequestBody Map<String, Object> payload) {
//        RequestEvent msg = new RequestEvent(
//                UUID.randomUUID().toString(),
//                "TESTE",
//                Instant.now(),
//                payload
//        );
//        enviarParaPedidos.execute(msg);
//    }
}