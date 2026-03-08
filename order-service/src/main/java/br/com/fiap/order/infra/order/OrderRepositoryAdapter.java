package br.com.fiap.order.infra.order;

import br.com.fiap.order.core.domain.Order;
import br.com.fiap.order.core.domain.OrderItem;
import br.com.fiap.order.core.gateway.OrderRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class OrderRepositoryAdapter implements OrderRepositoryPort {

    private final OrderJpaRepository jpa;

    public OrderRepositoryAdapter(OrderJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Order save(Order order) {
        OrderEntity entity = toEntity(order);
        List<OrderItemEntity> itemEntities = order.getItems().stream()
                .map(i -> toItemEntity(entity, i))
                .toList();
        entity.getItems().clear();
        entity.getItems().addAll(itemEntities);
        return toDomain(jpa.save(entity));
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return jpa.findById(id).map(this::toDomain);
    }

    @Override
    public List<Order> findByClientId(UUID clientId) {
        return jpa.findByClientId(clientId).stream()
                .map(this::toDomain)
                .toList();
    }

    private OrderEntity toEntity(Order order) {
        OrderEntity e = new OrderEntity();
        e.setId(order.getId());
        e.setClientId(order.getClientId());
        e.setRestaurantId(order.getRestaurantId());
        e.setStatus(order.getStatus());
        e.setTotal(order.getTotal());
        e.setCreatedAt(order.getCreatedAt());
        return e;
    }

    private OrderItemEntity toItemEntity(OrderEntity orderEntity, OrderItem item) {
        OrderItemEntity e = new OrderItemEntity();
        e.setOrder(orderEntity);
        e.setProductId(item.getProductId());
        e.setName(item.getName());
        e.setQuantity(item.getQuantity());
        e.setPrice(item.getPrice());
        e.setSubtotal(item.getSubtotal());
        return e;
    }

    private Order toDomain(OrderEntity e) {
        List<OrderItem> items = e.getItems().stream()
                .map(i -> new OrderItem(i.getProductId(), i.getName(), i.getQuantity(), i.getPrice()))
                .toList();
        return new Order(e.getId(), e.getClientId(), e.getRestaurantId(),
                items, e.getStatus(), e.getTotal(), e.getCreatedAt());
    }
}
