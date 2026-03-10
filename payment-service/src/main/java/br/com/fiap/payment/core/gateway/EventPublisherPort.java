package br.com.fiap.payment.core.gateway;

import br.com.fiap.payment.core.domain.Payment;

public interface EventPublisherPort {
    void publishPaymentApproved(Payment payment);
    void publishPaymentPending(Payment payment, String reason);
}
