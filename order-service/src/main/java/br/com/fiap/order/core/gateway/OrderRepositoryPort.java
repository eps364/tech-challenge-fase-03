package br.com.fiap.order.core.gateway;

import br.com.fiap.order.core.domain.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepositoryPort {
    Order save(Order order);
    Optional<Order> findById(UUID id);
    List<Order> findByClientId(UUID clientId);
}
