package br.com.fiap.restaurant.infra.adapters.inbound.messaging;

import br.com.fiap.restaurant.core.dto.OrderPaidForPreparationEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class OrderPaidForPreparationListener {

    private static final Logger log = LoggerFactory.getLogger(OrderPaidForPreparationListener.class);

    private final ObjectMapper objectMapper;

    public OrderPaidForPreparationListener(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "${app.rabbit.queues.in.orderPaidRestaurant}")
    public void handle(Message message) {
        String payload = new String(message.getBody(), StandardCharsets.UTF_8);

        try {
            OrderPaidForPreparationEvent event =
                    objectMapper.readValue(payload, OrderPaidForPreparationEvent.class);

            log.info(
                    "Pedido pago recebido para preparação. orderId={}, restaurantId={}, clientId={}, total={}, status={}",
                    event.orderId(),
                    event.restaurantId(),
                    event.clientId(),
                    event.total(),
                    event.status()
            );

            log.info("Restaurante pode iniciar a preparação do pedido {}", event.orderId());

        } catch (Exception e) {
            log.error("Erro ao converter mensagem de pedido pago. payload={}", payload, e);
            throw new IllegalArgumentException("Mensagem inválida para OrderPaidForPreparationEvent", e);
        }
    }
}