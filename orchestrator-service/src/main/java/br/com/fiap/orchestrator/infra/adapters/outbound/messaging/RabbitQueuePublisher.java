package br.com.fiap.orchestrator.infra.adapters.outbound.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import br.com.fiap.orchestrator.core.dto.messaging.QueueMessage;
import br.com.fiap.orchestrator.core.gateway.QueuePublisherPort;

@Component
public class RabbitQueuePublisher implements QueuePublisherPort {

    private final RabbitTemplate rabbitTemplate;

    public RabbitQueuePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publish(String routingKey, QueueMessage message) {
        rabbitTemplate.convertAndSend(routingKey, message);
    }
}