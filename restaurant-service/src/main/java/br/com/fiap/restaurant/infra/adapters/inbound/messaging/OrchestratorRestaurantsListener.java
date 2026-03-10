package br.com.fiap.restaurant.infra.adapters.inbound.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import br.com.fiap.restaurant.core.dto.QueueMessage;
import br.com.fiap.restaurant.core.gateway.QueuePublisherPort;
import br.com.fiap.restaurant.core.usecase.ProcessOrchestratorRequestUseCase;import br.com.fiap.restaurant.infra.config.RabbitQueuesProperties;
import br.com.fiap.restaurant.infra.messaging.mapper.MessageMapper;

@Component
public class OrchestratorRestaurantsListener {

    private final ProcessOrchestratorRequestUseCase useCase;
    private final QueuePublisherPort publisher;
    private final RabbitQueuesProperties props;

    public OrchestratorRestaurantsListener(ProcessOrchestratorRequestUseCase useCase,
                                            QueuePublisherPort publisher,
                                            RabbitQueuesProperties props) {
        this.useCase = useCase;
        this.publisher = publisher;
        this.props = props;
    }

    @RabbitListener(queues = "${app.rabbit.queues.in.orchestratorRestaurants}")
    public void onMessage(QueueMessage msg) {
        var cmd = MessageMapper.toCommand(msg);
        var result = useCase.execute(cmd);

        var responseMessage = MessageMapper.toResponseMessage(result);
        publisher.publish(props.queues().out().restaurantsOrchestrator(), responseMessage);
    }
}