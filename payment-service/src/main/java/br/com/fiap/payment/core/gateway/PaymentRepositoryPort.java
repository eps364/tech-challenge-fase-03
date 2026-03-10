package br.com.fiap.payment.core.gateway;

import java.util.Optional;
import java.util.UUID;

import br.com.fiap.payment.core.domain.Payment;

public interface PaymentRepositoryPort {
    Payment save(Payment payment);
    Optional<Payment> findByOrderId(UUID orderId);
}
