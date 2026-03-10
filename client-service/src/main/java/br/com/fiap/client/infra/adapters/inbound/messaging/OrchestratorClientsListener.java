package br.com.fiap.client.infra.adapters.inbound.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import br.com.fiap.client.core.dto.QueueMessage;
import br.com.fiap.client.core.gateway.QueuePublisherPort;
import br.com.fiap.client.core.usecase.ProcessOrchestratorRequestUseCase;import br.com.fiap.client.infra.config.RabbitQueuesProperties;
import br.com.fiap.client.infra.messaging.mapper.MessageMapper;

@Component
public class OrchestratorClientsListener {

    private final ProcessOrchestratorRequestUseCase useCase;
    private final QueuePublisherPort publisher;
    private final RabbitQueuesProperties props;

    public OrchestratorClientsListener(ProcessOrchestratorRequestUseCase useCase,
                                        QueuePublisherPort publisher,
                                        RabbitQueuesProperties props) {
        this.useCase = useCase;
        this.publisher = publisher;
        this.props = props;
    }

    @RabbitListener(queues = "${app.rabbit.queues.in.orchestratorClients}")
    public void onMessage(QueueMessage msg) {
        var cmd = MessageMapper.toCommand(msg);
        var result = useCase.execute(cmd);

        var responseMessage = MessageMapper.toResponseMessage(result);
        publisher.publish(props.queues().out().clientsOrchestrator(), responseMessage);
    }
}