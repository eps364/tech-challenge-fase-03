package br.com.fiap.orchestrator.core.usecase;

import br.com.fiap.orchestrator.core.dto.messaging.QueueMessage;
import br.com.fiap.orchestrator.core.gateway.QueuePublisherPort;
import br.com.fiap.orchestrator.core.valueobject.RoutingKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendToRestaurantsUseCase {

    private static final Logger logger = LoggerFactory.getLogger(SendToRestaurantsUseCase.class);

    private final QueuePublisherPort publisher;

    public SendToRestaurantsUseCase(QueuePublisherPort publisher) {
        this.publisher = publisher;
    }

    public void execute(QueueMessage message) {
        logger.info("Sending message to restaurants with type '{}'", message.type());
        try {
            publisher.publish(RoutingKeys.ORCHESTRATOR_RESTAURANTS_QUEUE, message);
            logger.info("Message sent to restaurants successfully");
        } catch (Exception e) {
            logger.error("Error sending message to restaurants: {}", e.getMessage(), e);
            throw e;
        }
    }
}