package br.com.fiap.order.core.usecase;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.fiap.order.core.domain.Order;
import br.com.fiap.order.core.domain.OrderItem;
import br.com.fiap.order.core.domain.OrderStatus;
import br.com.fiap.order.core.dto.requests.order.CreateOrderItemRequest;
import br.com.fiap.order.core.dto.requests.order.CreateOrderRequest;
import br.com.fiap.order.core.dto.responses.OrderItemResponse;
import br.com.fiap.order.core.dto.responses.OrderResponse;
import br.com.fiap.order.core.gateway.EventPublisherPort;
import br.com.fiap.order.core.gateway.OrderRepositoryPort;

public class CreateOrderUseCase {

    private static final Logger logger = LoggerFactory.getLogger(CreateOrderUseCase.class);

    private final OrderRepositoryPort orderRepositoryPort;
    private final EventPublisherPort eventPublisherPort;

    public CreateOrderUseCase(OrderRepositoryPort orderRepositoryPort,
                              EventPublisherPort eventPublisherPort) {
        this.orderRepositoryPort = orderRepositoryPort;
        this.eventPublisherPort = eventPublisherPort;
    }

    public OrderResponse execute(CreateOrderRequest request) {
        logger.info("Creating order for client {}", request.clientId());
        List<OrderItem> items = buildItems(request.items());

        Order order = new Order(
                request.clientId(),
                request.restaurantId(),
                items,
                OrderStatus.CREATED,
                request.price(),
                request.requestDate()
        );

        Order saved = orderRepositoryPort.save(order);
        logger.info("Order saved with id {}", saved.getId());

        eventPublisherPort.publishOrderCreated(saved);
        logger.info("Order created event published for order id {}", saved.getId());

        return toResponse(saved);
    }

    private List<OrderItem> buildItems(List<CreateOrderItemRequest> requests) {
        return requests.stream()
                .map(this::toOrderItem)
                .toList();
    }

    private OrderItem toOrderItem(CreateOrderItemRequest request) {
        return new OrderItem(
                request.productId(),
                request.name(),
                request.quantity(),
                request.price()
        );
    }

    private OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getClientId(),
                order.getRestaurantId(),
                order.getItems().stream()
                        .map(item -> new OrderItemResponse(
                                item.getProductId(),
                                item.getName(),
                                item.getQuantity(),
                                item.getPrice(),
                                item.getSubtotal()
                        ))
                        .toList(),
                order.getStatus(),
                order.getTotal(),
                order.getCreatedAt()
        );
    }
}