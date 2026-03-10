package br.com.fiap.payment.core.usecase;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import br.com.fiap.payment.core.domain.Payment;
import br.com.fiap.payment.core.domain.PaymentStatus;
import br.com.fiap.payment.core.gateway.EventPublisherPort;
import br.com.fiap.payment.core.gateway.ExternalPaymentGatewayPort;
import br.com.fiap.payment.core.gateway.PaymentRepositoryPort;

public class ProcessPaymentUseCase {

    private final PaymentRepositoryPort repository;
    private final ExternalPaymentGatewayPort externalPaymentGateway;
    private final EventPublisherPort eventPublisher;
    private final int maxAttempts;

    public ProcessPaymentUseCase(PaymentRepositoryPort repository,
                                 ExternalPaymentGatewayPort externalPaymentGateway,
                                 EventPublisherPort eventPublisher,
                                 int maxAttempts) {
        this.repository = repository;
        this.externalPaymentGateway = externalPaymentGateway;
        this.eventPublisher = eventPublisher;
        this.maxAttempts = maxAttempts;
    }

    public void execute(Map<String, Object> payload, boolean fromWorker) {
        UUID orderId = UUID.fromString(String.valueOf(payload.get("orderId")));
        UUID clientId = UUID.fromString(String.valueOf(payload.get("clientId")));
        BigDecimal amount = new BigDecimal(String.valueOf(payload.get("total")));

        Payment payment = repository.findByOrderId(orderId)
                .orElseGet(() -> repository.save(Payment.newPending(orderId, clientId, amount)));

        if (payment.getStatus() == PaymentStatus.APPROVED) {
            return;
        }

        Payment attemptPayment = repository.save(payment.incrementAttempts());

        try {
            boolean approved = externalPaymentGateway.authorize(attemptPayment);
            if (approved) {
                Payment approvedPayment = repository.save(attemptPayment.withStatus(PaymentStatus.APPROVED));
                eventPublisher.publishPaymentApproved(approvedPayment);
                return;
            }
            handlePending(attemptPayment, fromWorker, "External processor did not approve payment");
        } catch (Exception ex) {
            handlePending(attemptPayment, fromWorker, ex.getMessage());
        }
    }

    private void handlePending(Payment payment, boolean fromWorker, String reason) {
        Payment pendingPayment = repository.save(payment.withStatus(PaymentStatus.PENDING));
        if (fromWorker && pendingPayment.getAttempts() >= maxAttempts) {
            repository.save(pendingPayment.withStatus(PaymentStatus.FAILED));
            return;
        }
        eventPublisher.publishPaymentPending(pendingPayment, Optional.ofNullable(reason).orElse("Unavailable"));
    }
}
