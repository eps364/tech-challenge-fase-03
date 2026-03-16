package br.com.fiap.payment.core.gateway;

import java.util.UUID;

import br.com.fiap.payment.core.domain.Payment;
import br.com.fiap.payment.core.domain.ProcPagPaymentStatus;

public interface ProcPagGatewayPort {
    void requestPayment(Payment payment);
    ProcPagPaymentStatus getPaymentStatus(UUID paymentId);
}