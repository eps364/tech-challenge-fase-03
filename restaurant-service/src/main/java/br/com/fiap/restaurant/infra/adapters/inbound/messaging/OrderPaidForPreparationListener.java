package br.com.fiap.restaurant.infra.adapters.inbound.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import br.com.fiap.restaurant.core.dto.OrderPaidForPreparationEvent;

@Component
public class OrderPaidForPreparationListener {

    private static final Logger log = LoggerFactory.getLogger(OrderPaidForPreparationListener.class);

    @RabbitListener(queues = "${app.rabbit.queues.in.orderPaidRestaurant}")
    public void handle(OrderPaidForPreparationEvent event) {
        log.info(
                "Pedido pago recebido para preparação. orderId={}, restaurantId={}, clientId={}, total={}, status={}",
                event.orderId(),
                event.restaurantId(),
                event.clientId(),
                event.total(),
                event.status()
        );

        log.info("Restaurante pode iniciar a preparação do pedido {}", event.orderId());
    }
}