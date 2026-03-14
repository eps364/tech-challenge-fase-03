package br.com.fiap.order.core.usecase;

import br.com.fiap.order.core.domain.Order;
import br.com.fiap.order.core.dto.responses.OrderItemResponse;
import br.com.fiap.order.core.dto.responses.OrderResponse;
import br.com.fiap.order.core.gateway.OrderRepositoryPort;

import java.util.List;
import java.util.UUID;

public class ListOrdersUseCase {

    private final OrderRepositoryPort repo;

    public ListOrdersUseCase(OrderRepositoryPort repo) {
        this.repo = repo;
    }

    public List<OrderResponse> execute(UUID clientId) {
        return repo.findByClientId(clientId).stream()
                .map(this::toResponse)
                .toList();
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
