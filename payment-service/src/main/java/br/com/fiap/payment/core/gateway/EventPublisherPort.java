package br.com.fiap.payment.core.gateway;

import br.com.fiap.payment.core.domain.Payment;

public interface EventPublisherPort {
    void publishPaymentPending(Payment payment, String reason);
    void publishPaymentApproved(Payment payment);
    void publishPaymentFailed(Payment payment, String reason);
}