package br.com.fiap.orchestrator.infra.web.controller;

import br.com.fiap.orchestrator.core.dto.CreateOrderInput;
import br.com.fiap.orchestrator.core.usecase.CreateOrderOrchestrationUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orchestrations/orders")
public class OrchestratorController {

    private final CreateOrderOrchestrationUseCase createOrderOrchestrationUseCase;

    public OrchestratorController(CreateOrderOrchestrationUseCase createOrderOrchestrationUseCase) {
        this.createOrderOrchestrationUseCase = createOrderOrchestrationUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(
            @RequestBody CreateOrderInput input,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String clientId = jwt.getClaimAsString("sub");
        createOrderOrchestrationUseCase.execute(clientId, input);
        return ResponseEntity.ok().build();
    }
}