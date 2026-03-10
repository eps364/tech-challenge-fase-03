package br.com.fiap.orchestrator.infra.web.controller;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.orchestrator.core.dto.QueueMessage;
import br.com.fiap.orchestrator.core.usecase.SendToOrdersUseCase;
@RestController
@RequestMapping("/orchestrator")
public class OrchestratorController {

    private final SendToOrdersUseCase sendToOrders;

    public OrchestratorController(SendToOrdersUseCase sendToOrders) {
        this.sendToOrders = sendToOrders;
    }

    @PostMapping("/test/orders")
    public void testOrders(@RequestBody Map<String, Object> payload) {
        QueueMessage message = new QueueMessage(
                UUID.randomUUID().toString(),
                "TEST",
                Instant.now().toString(),
                payload
        );
        sendToOrders.execute(message);
    }
}