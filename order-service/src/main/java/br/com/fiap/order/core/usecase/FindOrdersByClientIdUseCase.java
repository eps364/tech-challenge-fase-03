package br.com.fiap.order.core.usecase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.fiap.order.core.domain.Order;
import br.com.fiap.order.core.dto.responses.OrderItemResponse;
import br.com.fiap.order.core.dto.responses.OrderResponse;
import br.com.fiap.order.core.gateway.OrderRepositoryPort;

import java.util.List;
import java.util.UUID;

public class FindOrdersByClientIdUseCase {

    private static final Logger logger = LoggerFactory.getLogger(FindOrdersByClientIdUseCase.class);

    private final OrderRepositoryPort orderRepositoryPort;

    public FindOrdersByClientIdUseCase(OrderRepositoryPort orderRepositoryPort) {
        this.orderRepositoryPort = orderRepositoryPort;
    }

    public List<OrderResponse> execute(UUID clientId) {
        logger.info("Finding orders for client {}", clientId);
        List<OrderResponse> orders = orderRepositoryPort.findByClientId(clientId)
                .stream()
                .map(this::toResponse)
                .toList();
        logger.info("Found {} orders for client {}", orders.size(), clientId);
        return orders;
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
