package br.com.fiap.payment.core.usecase;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import br.com.fiap.payment.core.domain.Payment;
import br.com.fiap.payment.core.domain.PaymentStatus;
import br.com.fiap.payment.core.gateway.EventPublisherPort;
import br.com.fiap.payment.core.gateway.ExternalPaymentGatewayPort;
import br.com.fiap.payment.core.gateway.PaymentRepositoryPort;

public class ProcessPaymentUseCase {

    private static final Logger logger = LoggerFactory.getLogger(ProcessPaymentUseCase.class);

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

        logger.info("Processing payment for order {} from client {} amount {} (fromWorker: {})", orderId, clientId, amount, fromWorker);

        Payment payment = repository.findByOrderId(orderId)
                .orElseGet(() -> {
                    logger.debug("Creating new pending payment for order {}", orderId);
                    return repository.save(Payment.newPending(orderId, clientId, amount));
                });

        if (payment.getStatus() == PaymentStatus.APPROVED) {
            logger.info("Payment already approved for order {}", orderId);
            return;
        }

        logger.debug("Incrementing attempts for payment {}", payment.getId());
        Payment attemptPayment = repository.save(payment.incrementAttempts());

        try {
            logger.debug("Calling external payment gateway for payment {}", attemptPayment.getId());
            boolean approved = externalPaymentGateway.authorize(attemptPayment);
            if (approved) {
                logger.info("Payment approved for order {}", orderId);
                Payment approvedPayment = repository.save(attemptPayment.withStatus(PaymentStatus.APPROVED));
                eventPublisher.publishPaymentApproved(approvedPayment);
                return;
            }
            handlePending(attemptPayment, fromWorker, "External processor did not approve payment");
        } catch (Exception ex) {
            logger.error("Error processing payment for order {}: {}", orderId, ex.getMessage(), ex);
            handlePending(attemptPayment, fromWorker, ex.getMessage());
        }
    }

    private void handlePending(Payment payment, boolean fromWorker, String reason) {
        logger.warn("Handling pending payment for order {}: {}", payment.getOrderId(), reason);
        Payment pendingPayment = repository.save(payment.withStatus(PaymentStatus.PENDING));
        if (fromWorker && pendingPayment.getAttempts() >= maxAttempts) {
            logger.error("Max attempts reached for payment {}, marking as failed", payment.getId());
            repository.save(pendingPayment.withStatus(PaymentStatus.FAILED));
            return;
        }
        eventPublisher.publishPaymentPending(pendingPayment, Optional.ofNullable(reason).orElse("Unavailable"));
    }
}
