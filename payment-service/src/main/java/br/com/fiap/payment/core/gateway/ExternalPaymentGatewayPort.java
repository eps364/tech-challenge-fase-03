package br.com.fiap.payment.core.gateway;

import br.com.fiap.payment.core.domain.Payment;

public interface ExternalPaymentGatewayPort {
    boolean authorize(Payment payment);
}
