package br.com.fiap.payment.core.usecase;

import java.util.UUID;

import br.com.fiap.payment.core.dto.PaymentResponse;
import br.com.fiap.payment.core.gateway.PaymentRepositoryPort;

public class GetPaymentByOrderIdUseCase {
    private final PaymentRepositoryPort repository;

    public GetPaymentByOrderIdUseCase(PaymentRepositoryPort repository) {
        this.repository = repository;
    }

    public PaymentResponse execute(UUID orderId) {
        return repository.findByOrderId(orderId)
                .map(p -> new PaymentResponse(
                        p.getId(),
                        p.getOrderId(),
                        p.getClientId(),
                        p.getAmount(),
                        p.getStatus(),
                        null // Or get from domain if available
                ))
                .orElseThrow(() -> new RuntimeException("Payment not found for order: " + orderId));
    }
}
