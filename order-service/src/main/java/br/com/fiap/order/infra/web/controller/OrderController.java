package br.com.fiap.order.infra.web.controller;

import br.com.fiap.order.core.dto.OrderRequest;
import br.com.fiap.order.core.dto.OrderResponse;
import br.com.fiap.order.core.usecase.confirmorder.ConfirmOrderUseCase;
import br.com.fiap.order.core.usecase.createorder.CreateOrderUseCase;
import br.com.fiap.order.core.usecase.getorder.GetOrderUseCase;
import br.com.fiap.order.core.usecase.listorders.ListOrdersUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final CreateOrderUseCase createOrder;
    private final GetOrderUseCase getOrder;
    private final ListOrdersUseCase listOrders;
    private final ConfirmOrderUseCase confirmOrder;

    public OrderController(CreateOrderUseCase createOrder,
                           GetOrderUseCase getOrder,
                           ListOrdersUseCase listOrders,
                           ConfirmOrderUseCase confirmOrder) {
        this.createOrder = createOrder;
        this.getOrder = getOrder;
        this.listOrders = listOrders;
        this.confirmOrder = confirmOrder;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@AuthenticationPrincipal Jwt jwt,
                                                @Valid @RequestBody OrderRequest request) {
        UUID clientId = UUID.fromString(jwt.getSubject());
        OrderResponse response = createOrder.execute(clientId, request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
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

    @PostMapping("/{id}/confirm")
    public ResponseEntity<OrderResponse> confirm(@PathVariable UUID id) {
        return ResponseEntity.ok(confirmOrder.execute(id));
    }
}