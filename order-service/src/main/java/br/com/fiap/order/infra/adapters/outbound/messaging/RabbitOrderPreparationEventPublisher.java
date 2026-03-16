package br.com.fiap.order.infra.adapters.outbound.messaging;

import br.com.fiap.order.core.dto.messaging.OrderPaidForPreparationEvent;
import br.com.fiap.order.core.gateway.OrderPreparationEventPublisherPort;
import br.com.fiap.order.infra.config.RabbitQueuesProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitOrderPreparationEventPublisher implements OrderPreparationEventPublisherPort {

    private static final Logger logger = LoggerFactory.getLogger(RabbitOrderPreparationEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;
    private final RabbitQueuesProperties rabbitQueuesProperties;
    private final ObjectMapper objectMapper;

    public RabbitOrderPreparationEventPublisher(RabbitTemplate rabbitTemplate,
                                                RabbitQueuesProperties rabbitQueuesProperties,
                                                ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitQueuesProperties = rabbitQueuesProperties;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(OrderPaidForPreparationEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);

            rabbitTemplate.convertAndSend(
                    rabbitQueuesProperties.getQueues().getOut().getOrderPaidRestaurant(),
                    payload,
                    message -> {
                        message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);
                        message.getMessageProperties().setContentEncoding("UTF-8");
                        message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                        message.getMessageProperties().getHeaders().remove("__TypeId__");
                        return message;
                    }
            );

            logger.info("Order paid for preparation event published for order {}", event.orderId());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao publicar evento de pedido pago para preparação", e);
        }
    }
}