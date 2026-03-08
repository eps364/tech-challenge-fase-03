package br.com.fiap.order.core.gateway;

import br.com.fiap.order.core.domain.Order;

public interface EventPublisherPort {
    void publishOrderCreated(Order order);
}
