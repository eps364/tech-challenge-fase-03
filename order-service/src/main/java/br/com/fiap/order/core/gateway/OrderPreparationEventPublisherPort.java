package br.com.fiap.order.core.gateway;

import br.com.fiap.order.core.dto.messaging.OrderPaidForPreparationEvent;

public interface OrderPreparationEventPublisherPort {
    void publish(OrderPaidForPreparationEvent event);
}