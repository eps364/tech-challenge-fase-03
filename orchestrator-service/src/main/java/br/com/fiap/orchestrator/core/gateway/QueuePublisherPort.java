package br.com.fiap.orchestrator.core.gateway;

import br.com.fiap.orchestrator.core.dto.messaging.QueueMessage;

public interface QueuePublisherPort {
    void publish(String routingKey, QueueMessage message);
}