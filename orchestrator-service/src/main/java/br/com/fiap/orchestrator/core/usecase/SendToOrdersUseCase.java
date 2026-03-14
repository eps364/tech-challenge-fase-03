package br.com.fiap.orchestrator.core.usecase;
import br.com.fiap.orchestrator.core.dto.messaging.QueueMessage;
import br.com.fiap.orchestrator.core.gateway.QueuePublisherPort;
import br.com.fiap.orchestrator.core.valueobject.RoutingKeys;

public class SendToOrdersUseCase {

    private final QueuePublisherPort publisher;

    public SendToOrdersUseCase(QueuePublisherPort publisher) {
        this.publisher = publisher;
    }

    public void execute(QueueMessage message) {
        publisher.publish(RoutingKeys.ORCHESTRATOR_ORDERS_QUEUE, message);
    }
}