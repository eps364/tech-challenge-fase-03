package br.com.fiap.payment.core.usecase;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.fiap.payment.core.domain.Payment;
import br.com.fiap.payment.core.domain.PaymentStatus;
import br.com.fiap.payment.core.domain.ProcPagPaymentStatus;
import br.com.fiap.payment.core.gateway.EventPublisherPort;
import br.com.fiap.payment.core.gateway.PaymentRepositoryPort;
import br.com.fiap.payment.core.gateway.ProcPagGatewayPort;

public class PollPendingPaymentsUseCase {

    private static final Logger logger = LoggerFactory.getLogger(PollPendingPaymentsUseCase.class);

    private final PaymentRepositoryPort repository;
    private final ProcPagGatewayPort procPagGatewayPort;
    private final EventPublisherPort eventPublisherPort;
    private final int maxAttempts;

    public PollPendingPaymentsUseCase(PaymentRepositoryPort repository,
                                      ProcPagGatewayPort procPagGatewayPort,
                                      EventPublisherPort eventPublisherPort,
                                      int maxAttempts) {
        this.repository = repository;
        this.procPagGatewayPort = procPagGatewayPort;
        this.eventPublisherPort = eventPublisherPort;
        this.maxAttempts = maxAttempts;
    }

    public void execute() {
        List<Payment> pendingPayments = repository.findPendingPayments();

        for (Payment payment : pendingPayments) {
            process(payment);
        }
    }

    private void process(Payment payment) {
        Payment attemptedPayment = repository.save(payment.incrementAttempts());

        try {
            ProcPagPaymentStatus procPagStatus = procPagGatewayPort.getPaymentStatus(attemptedPayment.getId());

            switch (procPagStatus) {
                case APPROVED -> handleApproved(attemptedPayment);
                case FAILED -> handleFailed(attemptedPayment, "Pagamento recusado pelo ProcPag");
                case PENDING -> handleStillPending(attemptedPayment);
            }
        } catch (Exception ex) {
            logger.warn("Erro ao consultar status do pagamento {}: {}", attemptedPayment.getId(), ex.getMessage());
            handleStillPending(attemptedPayment);
        }
    }

    private void handleApproved(Payment payment) {
        Payment approvedPayment = repository.save(payment.withStatus(PaymentStatus.APPROVED));
        eventPublisherPort.publishPaymentApproved(approvedPayment);
    }

    private void handleFailed(Payment payment, String reason) {
        Payment failedPayment = repository.save(payment.withStatus(PaymentStatus.FAILED));
        eventPublisherPort.publishPaymentFailed(failedPayment, reason);
    }

    private void handleStillPending(Payment payment) {
        if (payment.getAttempts() >= maxAttempts) {
            handleFailed(payment, "Número máximo de tentativas atingido");
            return;
        }

        repository.save(payment.withStatus(PaymentStatus.PENDING));
    }
}