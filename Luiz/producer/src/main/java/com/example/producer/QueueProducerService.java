package com.example.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class QueueProducerService {

    private final RabbitTemplate rabbitTemplate;
    private final String queue;

    public QueueProducerService(
            RabbitTemplate rabbitTemplate,
            @Value("${app.queue}") String queue
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.queue = queue;
    }

    public void send(String msg) {
        rabbitTemplate.convertAndSend(queue, msg);
    }
}
