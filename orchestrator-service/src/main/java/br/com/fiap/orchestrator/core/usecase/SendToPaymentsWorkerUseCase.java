package br.com.fiap.orchestrator.core.usecase;
import br.com.fiap.orchestrator.core.dto.messaging.QueueMessage;
import br.com.fiap.orchestrator.core.gateway.QueuePublisherPort;
import br.com.fiap.orchestrator.core.valueobject.RoutingKeys;

public class SendToPaymentsWorkerUseCase {

    private final QueuePublisherPort publisher;

    public SendToPaymentsWorkerUseCase(QueuePublisherPort publisher) {
        this.publisher = publisher;
    }

    public void execute(QueueMessage message) {
        publisher.publish(RoutingKeys.ORCHESTRATOR_PAYMENTS_WORKER_QUEUE, message);
    }
}
