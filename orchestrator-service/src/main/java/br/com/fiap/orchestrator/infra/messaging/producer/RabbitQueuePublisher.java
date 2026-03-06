package br.com.fiap.orchestrator.infra.messaging.producer;

import br.com.fiap.orchestrator.core.dto.RequestEvent;
import br.com.fiap.orchestrator.core.gateway.QueuePublisherPort;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitQueuePublisher implements QueuePublisherPort {

    private final RabbitTemplate rabbitTemplate;

    public RabbitQueuePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publish(String routingKey, RequestEvent mensagem) {
        rabbitTemplate.convertAndSend(routingKey, mensagem);
    }
}