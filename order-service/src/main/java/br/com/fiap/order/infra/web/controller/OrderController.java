package br.com.fiap.order.infra.web.controller;


import br.com.fiap.order.core.dto.requests.order.CreateOrderRequest;
import br.com.fiap.order.core.dto.responses.OrderResponse;
import br.com.fiap.order.core.usecase.CreateOrderUseCase;
import br.com.fiap.order.core.usecase.FindOrderByIdUseCase;
import br.com.fiap.order.core.usecase.FindOrdersByClientIdUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

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
        return ResponseEntity.ok(createOrderUseCase.execute(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(findOrderByIdUseCase.execute(id));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<OrderResponse>> findByClientId(@PathVariable UUID clientId) {
        return ResponseEntity.ok(findOrdersByClientIdUseCase.execute(clientId));
    }
}
