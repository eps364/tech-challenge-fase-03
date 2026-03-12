package br.com.fiap.order.infra.web.controller;

import br.com.fiap.order.core.dto.CreateOrderRequest;
import br.com.fiap.order.core.dto.OrderResponse;
import br.com.fiap.order.core.usecase.CreateOrderUseCase;
import br.com.fiap.order.core.usecase.GetOrderUseCase;
import br.com.fiap.order.core.usecase.ListOrdersUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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

    private final CreateOrderUseCase createOrder;
    private final GetOrderUseCase getOrder;
    private final ListOrdersUseCase listOrders;

    public OrderController(CreateOrderUseCase createOrder,
                           GetOrderUseCase getOrder,
                           ListOrdersUseCase listOrders) {
        this.createOrder = createOrder;
        this.getOrder = getOrder;
        this.listOrders = listOrders;
    }

    @PostMapping("/orchestrated")
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody CreateOrderRequest request) {
        return ResponseEntity.ok(createOrder.execute(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(getOrder.execute(id));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> listMyOrders(@AuthenticationPrincipal Jwt jwt) {
        UUID clientId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(listOrders.execute(clientId));
    }
}