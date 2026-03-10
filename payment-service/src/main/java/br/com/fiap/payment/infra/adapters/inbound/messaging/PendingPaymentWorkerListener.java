package br.com.fiap.payment.infra.adapters.inbound.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import br.com.fiap.payment.core.dto.QueueMessage;
import br.com.fiap.payment.core.usecase.ProcessPaymentUseCase;
@Component
public class PendingPaymentWorkerListener {

    private final ProcessPaymentUseCase processPaymentUseCase;

    public PendingPaymentWorkerListener(ProcessPaymentUseCase processPaymentUseCase) {
        this.processPaymentUseCase = processPaymentUseCase;
    }

    @RabbitListener(queues = "${app.rabbit.queues.in.orchestratorPaymentsWorker}")
    public void onMessage(QueueMessage message) {
        if (!"PAYMENT_PENDING".equalsIgnoreCase(message.type())) {
            return;
        }
        processPaymentUseCase.execute(message.payload(), true);
    }
}
