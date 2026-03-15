package br.com.fiap.payment.core.usecase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.fiap.payment.core.domain.Payment;
import br.com.fiap.payment.core.domain.PaymentStatus;
import br.com.fiap.payment.core.dto.OrderCreatedEvent;
import br.com.fiap.payment.core.gateway.EventPublisherPort;
import br.com.fiap.payment.core.gateway.PaymentRepositoryPort;
import br.com.fiap.payment.core.gateway.ProcPagGatewayPort;

public class StartPaymentProcessUseCase {

    private static final Logger logger = LoggerFactory.getLogger(StartPaymentProcessUseCase.class);

    private final PaymentRepositoryPort repository;
    private final ProcPagGatewayPort procPagGatewayPort;
    private final EventPublisherPort eventPublisherPort;

    public StartPaymentProcessUseCase(PaymentRepositoryPort repository,
                                      ProcPagGatewayPort procPagGatewayPort,
                                      EventPublisherPort eventPublisherPort) {
        this.repository = repository;
        this.procPagGatewayPort = procPagGatewayPort;
        this.eventPublisherPort = eventPublisherPort;
    }

    public void execute(OrderCreatedEvent event) {
        repository.findByOrderId(event.orderId()).ifPresent(existing -> {
            logger.info("Pagamento já existente para o pedido {}", event.orderId());
            throw new PaymentAlreadyProcessedException();
        });

        Payment payment = repository.save(
                Payment.newPending(event.orderId(), event.clientId(), event.total())
        );

        try {
            procPagGatewayPort.requestPayment(payment);

            Payment pendingPayment = repository.save(payment.withStatus(PaymentStatus.PENDING));
            eventPublisherPort.publishPaymentPending(pendingPayment, "Solicitação criada no ProcPag");
        } catch (Exception ex) {
            logger.error("Erro ao criar solicitação de pagamento para pedido {}", event.orderId(), ex);
            Payment failedPayment = repository.save(payment.withStatus(PaymentStatus.FAILED));
            eventPublisherPort.publishPaymentFailed(failedPayment, "Falha ao criar solicitação no ProcPag");
        }
    }

    private static class PaymentAlreadyProcessedException extends RuntimeException {
    }
}