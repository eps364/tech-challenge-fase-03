package br.com.fiap.orchestrator.core.usecase;
import br.com.fiap.orchestrator.core.dto.QueueMessage;
import br.com.fiap.orchestrator.core.gateway.QueuePublisherPort;
import br.com.fiap.orchestrator.core.valueobject.RoutingKeys;

public class SendToRestaurantsUseCase {

    private final QueuePublisherPort publisher;

    public SendToRestaurantsUseCase(QueuePublisherPort publisher) {
        this.publisher = publisher;
    }

    public void execute(QueueMessage message) {
        publisher.publish(RoutingKeys.ORCHESTRATOR_RESTAURANTS_QUEUE, message);
    }
}