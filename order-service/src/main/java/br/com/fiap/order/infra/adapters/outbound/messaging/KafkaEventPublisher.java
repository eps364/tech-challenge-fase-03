package br.com.fiap.order.infra.adapters.outbound.messaging;

import java.time.Instant;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.fiap.order.core.domain.Order;
import br.com.fiap.order.core.dto.messaging.OrderCreatedEvent;
import br.com.fiap.order.core.gateway.EventPublisherPort;
import br.com.fiap.order.infra.config.KafkaTopicsProperties;

@Component
public class KafkaEventPublisher implements EventPublisherPort {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaTopicsProperties topicsProperties;
    private final ObjectMapper objectMapper;

    public KafkaEventPublisher(KafkaTemplate<String, String> kafkaTemplate,
                               KafkaTopicsProperties topicsProperties,
                               ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicsProperties = topicsProperties;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publishOrderCreated(Order order) {
        OrderCreatedEvent event = new OrderCreatedEvent(
                order.getId(),
                order.getClientId(),
                order.getTotal(),
                Instant.now()
        );

        try {
            String json = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(topicsProperties.getOrderCreated(), order.getId().toString(), json);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Erro ao serializar evento ORDER_CREATED", e);
        }
    }
}