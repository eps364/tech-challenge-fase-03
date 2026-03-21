package br.com.fiap.orchestrator.infra.web.controller;


import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.orchestrator.core.dto.requests.orchestration.CreateOrderRequest;
import br.com.fiap.orchestrator.core.dto.responses.OrderAcceptedResponse;
import br.com.fiap.orchestrator.core.usecase.CreateOrderOrchestrationUseCase;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/orchestrator")
public class RequestsController implements br.com.fiap.orchestrator.infra.web.controller.api.OrchestratorAPI {

    private static final Logger logger = LoggerFactory.getLogger(RequestsController.class);

    private final CreateOrderOrchestrationUseCase createOrderOrchestrationUseCase;

    public RequestsController(CreateOrderOrchestrationUseCase createOrderOrchestrationUseCase) {
        this.createOrderOrchestrationUseCase = createOrderOrchestrationUseCase;
    }

    @PostMapping("/requests")
    @Override
    public ResponseEntity<OrderAcceptedResponse> criarPedido(@RequestBody @Valid CreateOrderRequest event) {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID clientId = UUID.fromString(jwt.getSubject());
        logger.info("Received create order request from client {}: restaurantId={}, items={}", clientId, event.restaurantId(), event.items().size());
        try {
            var response = createOrderOrchestrationUseCase.execute(clientId, event);
            logger.info("Order orchestration completed successfully for client {}", clientId);
            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            logger.error("Error during order orchestration for client {}: {}", clientId, e.getMessage(), e);
            throw e;
        }
    }
}