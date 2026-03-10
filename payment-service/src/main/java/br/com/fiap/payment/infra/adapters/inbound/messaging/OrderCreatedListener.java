package br.com.fiap.payment.infra.adapters.inbound.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import br.com.fiap.payment.core.dto.QueueMessage;
import br.com.fiap.payment.core.usecase.ProcessPaymentUseCase;
@Component
public class OrderCreatedListener {

    private final ProcessPaymentUseCase processPaymentUseCase;

    public OrderCreatedListener(ProcessPaymentUseCase processPaymentUseCase) {
        this.processPaymentUseCase = processPaymentUseCase;
    }

    @RabbitListener(queues = "${app.rabbit.queues.in.orchestratorPayments}")
    public void onMessage(QueueMessage message) {
        if (!"ORDER_CREATED".equalsIgnoreCase(message.type())) {
            return;
        }
        processPaymentUseCase.execute(message.payload(), false);
    }
}
