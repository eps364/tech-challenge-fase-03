package br.com.fiap.orchestrator.infra.adapters.inbound.messaging;

import br.com.fiap.orchestrator.core.dto.QueueMessage;
import br.com.fiap.orchestrator.core.usecase.SendToPaymentsUseCase;
import br.com.fiap.orchestrator.core.valueobject.RoutingKeys;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RestaurantsReplyListener {

    private final SendToPaymentsUseCase sendToPayments;

    public RestaurantsReplyListener(SendToPaymentsUseCase sendToPayments) {
        this.sendToPayments = sendToPayments;
    }

    @RabbitListener(queues = RoutingKeys.RESTAURANTS_ORCHESTRATOR_QUEUE)
    public void onMessage(QueueMessage message) {
        if (!"ORDER_CREATED".equalsIgnoreCase(message.type())) {
            return;
        }
        sendToPayments.execute(message);
    }
}
