package br.com.fiap.order.core.usecase;

import br.com.fiap.order.core.domain.Order;
import br.com.fiap.order.core.dto.responses.OrderItemResponse;
import br.com.fiap.order.core.dto.responses.OrderResponse;
import br.com.fiap.order.core.gateway.OrderRepositoryPort;

import java.util.List;
import java.util.UUID;

public class GetOrderUseCase {

    private final OrderRepositoryPort repo;

    public GetOrderUseCase(OrderRepositoryPort repo) {
        this.repo = repo;
    }

    public OrderResponse execute(UUID id) {
        Order o = repo.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        return toResponse(o);
    }

    private OrderResponse toResponse(Order o) {
        List<OrderItemResponse> items = o.getItems().stream()
                .map(i -> new OrderItemResponse(i.getProductId(), i.getName(),
                        i.getQuantity(), i.getPrice(), i.getSubtotal()))
                .toList();
        return new OrderResponse(o.getId(), o.getClientId(), o.getRestaurantId(),
                items, o.getStatus(), o.getTotal(), o.getCreatedAt());
    }
}
