package br.com.fiap.order.infra.web.controller;


import java.util.List;
import java.util.UUID;

import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.order.core.dto.requests.order.CreateOrderRequest;
import br.com.fiap.order.core.dto.responses.OrderResponse;
import br.com.fiap.order.core.usecase.CreateOrderUseCase;
import br.com.fiap.order.core.usecase.FindOrderByIdUseCase;
import br.com.fiap.order.core.usecase.FindOrdersByClientIdUseCase;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
public class OrderController implements br.com.fiap.order.infra.web.controller.api.OrderAPI {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final CreateOrderUseCase createOrderUseCase;
    private final FindOrderByIdUseCase findOrderByIdUseCase;
    private final FindOrdersByClientIdUseCase findOrdersByClientIdUseCase;

    public OrderController(CreateOrderUseCase createOrderUseCase,
                           FindOrderByIdUseCase findOrderByIdUseCase,
                           FindOrdersByClientIdUseCase findOrdersByClientIdUseCase) {
        this.createOrderUseCase = createOrderUseCase;
        this.findOrderByIdUseCase = findOrderByIdUseCase;
        this.findOrdersByClientIdUseCase = findOrdersByClientIdUseCase;
    }

    @PostMapping("/orchestrated")
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody CreateOrderRequest request) {
        logger.info("Received create order request: clientId={}, restaurantId={}, items={}", request.clientId(), request.restaurantId(), request.items().size());
        try {
            OrderResponse response = createOrderUseCase.execute(request);
            logger.info("Order created successfully: {}", response.id());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error creating order: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findById(@PathVariable UUID id) {
        logger.info("Received find order by ID request: {}", id);
        try {
            OrderResponse response = findOrderByIdUseCase.execute(id);
            logger.info("Order found: {}", response.id());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error finding order {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<OrderResponse>> findByClientId(@PathVariable UUID clientId) {
        logger.info("Received find orders by client ID request: {}", clientId);
        try {
            List<OrderResponse> response = findOrdersByClientIdUseCase.execute(clientId);
            logger.info("Found {} orders for client {}", response.size(), clientId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error finding orders for client {}: {}", clientId, e.getMessage(), e);
            throw e;
        }
    }
}
