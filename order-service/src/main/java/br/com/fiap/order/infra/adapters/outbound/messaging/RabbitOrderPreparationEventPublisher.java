package br.com.fiap.order.infra.adapters.outbound.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import br.com.fiap.order.core.dto.messaging.OrderPaidForPreparationEvent;
import br.com.fiap.order.core.gateway.OrderPreparationEventPublisherPort;
import br.com.fiap.order.infra.config.RabbitQueuesProperties;

@Component
public class RabbitOrderPreparationEventPublisher implements OrderPreparationEventPublisherPort {

    private static final Logger logger = LoggerFactory.getLogger(RabbitOrderPreparationEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;
    private final RabbitQueuesProperties rabbitQueuesProperties;

    public RabbitOrderPreparationEventPublisher(RabbitTemplate rabbitTemplate,
                                                RabbitQueuesProperties rabbitQueuesProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitQueuesProperties = rabbitQueuesProperties;
    }

    @Override
    public void publish(OrderPaidForPreparationEvent event) {
        logger.info("Publishing order paid for preparation event for order {}", event.orderId());
        rabbitTemplate.convertAndSend(
                rabbitQueuesProperties.getQueues().getOut().getOrderPaidRestaurant(),
                event
        );
        logger.info("Order paid for preparation event published to queue for order {}", event.orderId());
    }
}