package br.com.fiap.restaurant.infra.messaging.producer;

import br.com.fiap.restaurant.core.dto.MensagemFila;
import br.com.fiap.restaurant.core.gateway.QueuePublisherPort;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitQueuePublisher implements QueuePublisherPort {

    private final RabbitTemplate rabbitTemplate;

    public RabbitQueuePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publish(String routingKey, MensagemFila mensagem) {
        rabbitTemplate.convertAndSend(routingKey, mensagem);
    }
}