package br.com.fiap.client.core.gateway;


import br.com.fiap.client.core.dto.QueueMessage;

public interface QueuePublisherPort {
    void publish(String routingKey, QueueMessage message);
}