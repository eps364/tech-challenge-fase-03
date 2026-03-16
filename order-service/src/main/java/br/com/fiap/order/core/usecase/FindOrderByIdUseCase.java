package br.com.fiap.order.core.usecase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.fiap.order.core.domain.Order;
import br.com.fiap.order.core.dto.responses.OrderItemResponse;
import br.com.fiap.order.core.dto.responses.OrderResponse;
import br.com.fiap.order.core.gateway.OrderRepositoryPort;

import java.util.UUID;

public class FindOrderByIdUseCase {

    private static final Logger logger = LoggerFactory.getLogger(FindOrderByIdUseCase.class);

    private final OrderRepositoryPort orderRepositoryPort;

    public FindOrderByIdUseCase(OrderRepositoryPort orderRepositoryPort) {
        this.orderRepositoryPort = orderRepositoryPort;
    }

    public OrderResponse execute(UUID id) {
        logger.info("Finding order by id {}", id);
        Order order = orderRepositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado: " + id));

        logger.info("Order found with id {}", id);
        return toResponse(order);
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
