package br.com.fiap.orchestrator.infra.adapters.outbound.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import br.com.fiap.orchestrator.core.dto.messaging.QueueMessage;
import br.com.fiap.orchestrator.core.gateway.QueuePublisherPort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class RabbitQueuePublisher implements QueuePublisherPort {

    private static final Logger logger = LoggerFactory.getLogger(RabbitQueuePublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public RabbitQueuePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publish(String routingKey, QueueMessage message) {
        logger.info("Publishing message to routing key '{}' with type '{}'", routingKey, message.type());
        try {
            rabbitTemplate.convertAndSend(routingKey, message);
            logger.info("Message published successfully to routing key '{}'", routingKey);
        } catch (Exception e) {
            logger.error("Error publishing message to routing key '{}': {}", routingKey, e.getMessage(), e);
            throw e;
        }
    }
}