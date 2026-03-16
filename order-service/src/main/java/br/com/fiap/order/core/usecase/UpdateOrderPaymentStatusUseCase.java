package br.com.fiap.order.core.usecase;

import java.time.Instant;
import java.util.UUID;

import br.com.fiap.order.core.domain.Order;
import br.com.fiap.order.core.domain.OrderStatus;
import br.com.fiap.order.core.dto.messaging.OrderPaidForPreparationEvent;
import br.com.fiap.order.core.gateway.OrderPreparationEventPublisherPort;
import br.com.fiap.order.core.gateway.OrderRepositoryPort;

public class UpdateOrderPaymentStatusUseCase {

    private final OrderRepositoryPort repository;
    private final OrderPreparationEventPublisherPort orderPreparationEventPublisherPort;

    public UpdateOrderPaymentStatusUseCase(OrderRepositoryPort repository,
                                           OrderPreparationEventPublisherPort orderPreparationEventPublisherPort) {
        this.repository = repository;
        this.orderPreparationEventPublisherPort = orderPreparationEventPublisherPort;
    }

    public void markAsPendingPayment(UUID orderId) {
        updateStatus(orderId, OrderStatus.PENDING_PAYMENT);
    }

    public void markAsPaid(UUID orderId) {
        Order order = repository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (order.getStatus() == OrderStatus.PAID) {
            return;
        }

        Order updated = repository.save(order.withStatus(OrderStatus.PAID));

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
    }

    public void markAsPaymentFailed(UUID orderId) {
        updateStatus(orderId, OrderStatus.PAYMENT_FAILED);
    }

    private void updateStatus(UUID orderId, OrderStatus newStatus) {
        Order order = repository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (order.getStatus() == OrderStatus.PAID) {
            return;
        }

        repository.save(order.withStatus(newStatus));
    }
}