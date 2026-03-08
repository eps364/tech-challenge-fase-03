package br.com.fiap.order.infra.messaging;

import br.com.fiap.order.core.domain.Order;
import br.com.fiap.order.core.domain.OrderItem;
import br.com.fiap.order.core.gateway.EventPublisherPort;
import br.com.fiap.order.infra.config.RabbitQueuesProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RabbitEventPublisher implements EventPublisherPort {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitQueuesProperties properties;

    public RabbitEventPublisher(RabbitTemplate rabbitTemplate, RabbitQueuesProperties properties) {
        this.rabbitTemplate = rabbitTemplate;
        this.properties = properties;
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
        message.put("tipo", "PEDIDO_CRIADO");
        message.put("timestamp", Instant.now().toString());
        message.put("payload", payload);

        rabbitTemplate.convertAndSend(
                properties.getExchange(),
                properties.getQueues().getOut().getPedidosOrquestrador(),
                message
        );
    }
}
