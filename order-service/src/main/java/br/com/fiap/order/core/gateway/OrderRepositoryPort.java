package br.com.fiap.order.core.gateway;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.fiap.order.core.domain.Order;

public interface OrderRepositoryPort {
    Order save(Order order);
    Optional<Order> findById(UUID id);
    List<Order> findByClientId(UUID clientId);
}
