package br.com.fiap.orchestrator.core.usecase;
import br.com.fiap.orchestrator.core.dto.QueueMessage;
import br.com.fiap.orchestrator.core.gateway.QueuePublisherPort;
import br.com.fiap.orchestrator.core.valueobject.RoutingKeys;

public class SendToClientsUseCase {

    private final QueuePublisherPort publisher;

    public SendToClientsUseCase(QueuePublisherPort publisher) {
        this.publisher = publisher;
    }

    public void execute(QueueMessage message) {
        publisher.publish(RoutingKeys.ORCHESTRATOR_CLIENTS_QUEUE, message);
    }
}