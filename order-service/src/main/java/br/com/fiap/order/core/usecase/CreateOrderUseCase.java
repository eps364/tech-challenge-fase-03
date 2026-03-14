package br.com.fiap.order.core.usecase;

import br.com.fiap.order.core.domain.Order;
import br.com.fiap.order.core.dto.requests.order.CreateOrderRequest;
import br.com.fiap.order.core.dto.responses.OrderItemResponse;
import br.com.fiap.order.core.dto.responses.OrderResponse;
import br.com.fiap.order.core.gateway.EventPublisherPort;
import br.com.fiap.order.core.gateway.OrderRepositoryPort;

import java.util.List;
import java.util.UUID;

public class CreateOrderUseCase {

    private final OrderRepositoryPort repo;
    private final EventPublisherPort eventPublisher;

    public CreateOrderUseCase(OrderRepositoryPort repo,
                              EventPublisherPort eventPublisher) {
        this.repo = repo;
        this.eventPublisher = eventPublisher;
    }

    public OrderResponse execute(CreateOrderRequest req) {
        Order order = Order.create(
                UUID.fromString(req.getClientId()),
                UUID.fromString(req.getRestaurantId()),
                req.getFoodId(),
                req.getQuantity(),
                req.getCpf(),
                req.getAddress(),
                req.getPrice(),
                req.getRequestDate()
        );

        Order saved = repo.save(order);
        eventPublisher.publishOrderCreated(saved);
        return toResponse(saved);
    }

    private OrderResponse toResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getItems().stream()
                .map(i -> new OrderItemResponse(
                        i.getProductId(),
                        i.getName(),
                        i.getQuantity(),
                        i.getPrice(),
                        i.getSubtotal()
                ))
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getClientId(),
                order.getRestaurantId(),
                itemResponses,
                order.getStatus(),
                order.getTotal(),
                order.getCreatedAt()
        );
    }
}