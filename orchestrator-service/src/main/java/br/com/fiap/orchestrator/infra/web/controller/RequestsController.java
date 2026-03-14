package br.com.fiap.orchestrator.infra.web.controller;


import br.com.fiap.orchestrator.core.dto.requests.orchestration.CreateOrderRequest;
import br.com.fiap.orchestrator.core.usecase.CreateOrderOrchestrationUseCase;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/orchestrator")
public class RequestsController {

    private final CreateOrderOrchestrationUseCase createOrderOrchestrationUseCase;

    public RequestsController(CreateOrderOrchestrationUseCase createOrderOrchestrationUseCase) {
        this.createOrderOrchestrationUseCase = createOrderOrchestrationUseCase;
    }

    @PostMapping("/requests")
    public void criarPedido(@RequestBody @Valid CreateOrderRequest event,
                            @AuthenticationPrincipal Jwt jwt) {
        UUID clientId = UUID.fromString(jwt.getSubject());
        createOrderOrchestrationUseCase.execute(clientId, event);
    }
}