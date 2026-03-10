package br.com.fiap.restaurant.core.gateway;


import br.com.fiap.restaurant.core.dto.QueueMessage;

public interface QueuePublisherPort {
    void publish(String routingKey, QueueMessage message);
}