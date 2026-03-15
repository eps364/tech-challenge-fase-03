package br.com.fiap.order.core.usecase;

import java.util.UUID;

import br.com.fiap.order.core.domain.Order;
import br.com.fiap.order.core.domain.OrderStatus;
import br.com.fiap.order.core.gateway.OrderRepositoryPort;

public class UpdateOrderPaymentStatusUseCase {

    private final OrderRepositoryPort repository;

    public UpdateOrderPaymentStatusUseCase(OrderRepositoryPort repository) {
        this.repository = repository;
    }

    public void markAsPendingPayment(UUID orderId) {
        updateStatus(orderId, OrderStatus.PENDING_PAYMENT);
    }

    public void markAsPaid(UUID orderId) {
        updateStatus(orderId, OrderStatus.PAID);
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