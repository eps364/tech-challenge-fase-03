package br.com.fiap.order.infra.adapters.outbound.messaging;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.fiap.order.core.domain.Order;
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
        List<Map<String, Object>> items = order.getItems().stream().map(i -> {
            Map<String, Object> item = new HashMap<>();
            item.put("productId", i.getProductId());
            item.put("name", i.getName());
            item.put("quantity", i.getQuantity());
            item.put("price", i.getPrice());
            item.put("subtotal", i.getSubtotal());
            return item;
        }).toList();

        Map<String, Object> payload = new HashMap<>();
        payload.put("orderId", order.getId().toString());
        payload.put("clientId", order.getClientId().toString());
        payload.put("restaurantId", order.getRestaurantId().toString());
        payload.put("items", items);
        payload.put("total", order.getTotal());
        payload.put("status", order.getStatus().name());
        payload.put("createdAt", order.getCreatedAt().toString());

        Map<String, Object> message = new HashMap<>();
        message.put("correlationId", order.getId().toString());
        message.put("type", "ORDER_CREATED");
        message.put("timestamp", Instant.now().toString());
        message.put("payload", payload);

        try {
            String json = objectMapper.writeValueAsString(message);
            kafkaTemplate.send(
                    topicsProperties.getOrderCreated(),
                    order.getId().toString(),
                    json
            );
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Erro ao serializar evento ORDER_CREATED", e);
        }
    }
}