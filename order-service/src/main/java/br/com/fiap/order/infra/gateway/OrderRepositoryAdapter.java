package br.com.fiap.order.infra.gateway;

import br.com.fiap.order.core.domain.Order;
import br.com.fiap.order.core.domain.OrderItem;
import br.com.fiap.order.core.gateway.OrderRepositoryPort;
import br.com.fiap.order.infra.entity.OrderEntity;
import br.com.fiap.order.infra.entity.OrderItemEmbeddable;
import br.com.fiap.order.infra.repository.OrderJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class OrderRepositoryAdapter implements OrderRepositoryPort {

    private final OrderJpaRepository orderJpaRepository;

    public OrderRepositoryAdapter(OrderJpaRepository orderJpaRepository) {
        this.orderJpaRepository = orderJpaRepository;
    }

    @Override
    public Order save(Order order) {
        OrderEntity entity = toEntity(order);
        OrderEntity saved = orderJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return orderJpaRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public List<Order> findByClientId(UUID clientId) {
        return orderJpaRepository.findByClientId(clientId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private OrderEntity toEntity(Order order) {
        List<OrderItemEmbeddable> items = order.getItems().stream()
                .map(item -> new OrderItemEmbeddable(
                        item.getProductId(),
                        item.getName(),
                        item.getQuantity(),
                        item.getPrice(),
                        item.getSubtotal()
                ))
                .toList();

        return new OrderEntity(
                order.getId(),
                order.getClientId(),
                order.getRestaurantId(),
                order.getStatus(),
                order.getTotal(),
                order.getCreatedAt(),
                items
        );
    }

    private Order toDomain(OrderEntity entity) {
        List<OrderItem> items = entity.getItems().stream()
                .map(item -> new OrderItem(
                        item.getProductId(),
                        item.getName(),
                        item.getQuantity(),
                        item.getPrice()
                ))
                .toList();

        return new Order(
                entity.getId(),
                entity.getClientId(),
                entity.getRestaurantId(),
                items,
                entity.getStatus(),
                entity.getTotal(),
                entity.getCreatedAt()
        );
    }
}
