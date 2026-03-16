package br.com.fiap.order.core.usecase;

import java.time.Instant;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.fiap.order.core.domain.Order;
import br.com.fiap.order.core.domain.OrderStatus;
import br.com.fiap.order.core.dto.messaging.OrderPaidForPreparationEvent;
import br.com.fiap.order.core.gateway.OrderPreparationEventPublisherPort;
import br.com.fiap.order.core.gateway.OrderRepositoryPort;

public class UpdateOrderPaymentStatusUseCase {

    private static final Logger logger = LoggerFactory.getLogger(UpdateOrderPaymentStatusUseCase.class);

    private final OrderRepositoryPort repository;
    private final OrderPreparationEventPublisherPort orderPreparationEventPublisherPort;

    public UpdateOrderPaymentStatusUseCase(OrderRepositoryPort repository,
                                           OrderPreparationEventPublisherPort orderPreparationEventPublisherPort) {
        this.repository = repository;
        this.orderPreparationEventPublisherPort = orderPreparationEventPublisherPort;
    }

    public void markAsPendingPayment(UUID orderId) {
        logger.info("Marking order {} as pending payment", orderId);
        updateStatus(orderId, OrderStatus.PENDING_PAYMENT);
    }

    public void markAsPaid(UUID orderId) {
        logger.info("Marking order {} as paid", orderId);
        Order order = repository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (order.getStatus() == OrderStatus.PAID) {
            logger.warn("Order {} is already paid", orderId);
            return;
        }

        Order updated = repository.save(order.withStatus(OrderStatus.PAID));
        logger.info("Order {} status updated to PAID", orderId);

        OrderPaidForPreparationEvent event = new OrderPaidForPreparationEvent(
                UUID.randomUUID(),
                Instant.now(),
                updated.getId(),
                updated.getRestaurantId(),
                updated.getClientId(),
                updated.getTotal(),
                updated.getStatus().name()
        );

        orderPreparationEventPublisherPort.publish(event);
        logger.info("Order paid event published for order {}", orderId);
    }

    public void markAsPaymentFailed(UUID orderId) {
        logger.info("Marking order {} as payment failed", orderId);
        updateStatus(orderId, OrderStatus.PAYMENT_FAILED);
    }

    private void updateStatus(UUID orderId, OrderStatus newStatus) {
        Order order = repository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (order.getStatus() == OrderStatus.PAID) {
            logger.warn("Cannot update status of already paid order {}", orderId);
            return;
        }

        repository.save(order.withStatus(newStatus));
        logger.info("Order {} status updated to {}", orderId, newStatus);
    }
}