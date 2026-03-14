package br.com.fiap.orchestrator.infra.adapters.inbound.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import br.com.fiap.orchestrator.core.dto.messaging.QueueMessage;
import br.com.fiap.orchestrator.core.usecase.SendToPaymentsUseCase;import br.com.fiap.orchestrator.core.valueobject.RoutingKeys;

@Component
public class OrdersReplyListener {

    private final SendToPaymentsUseCase sendToPayments;

    public OrdersReplyListener(SendToPaymentsUseCase sendToPayments) {
        this.sendToPayments = sendToPayments;
    }

    @RabbitListener(queues = RoutingKeys.ORDERS_ORCHESTRATOR_QUEUE)
    public void onMessage(QueueMessage message) {
        if (!"ORDER_CREATED".equalsIgnoreCase(message.type())) {
            return;
        }
        sendToPayments.execute(message);
    }
}
