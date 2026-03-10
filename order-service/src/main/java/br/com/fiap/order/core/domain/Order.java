package br.com.fiap.order.core.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class Order {
    private final UUID id;
    private final UUID clientId;
    private final UUID restaurantId;
    private final List<OrderItem> items;
    private final OrderStatus status;
    private final BigDecimal total;
    private final Instant createdAt;

    public Order(UUID id, UUID clientId, UUID restaurantId,
                 List<OrderItem> items, OrderStatus status,
                 BigDecimal total, Instant createdAt) {
        this.id = id;
        this.clientId = clientId;
        this.restaurantId = restaurantId;
        this.items = items;
        this.status = status;
        this.total = total;
        this.createdAt = createdAt;
    }

    public UUID getId()           { return id; }
    public UUID getClientId()     { return clientId; }
    public UUID getRestaurantId() { return restaurantId; }
    public List<OrderItem> getItems() { return items; }
    public OrderStatus getStatus() { return status; }
    public BigDecimal getTotal()  { return total; }
    public Instant getCreatedAt() { return createdAt; }

    public Order withStatus(OrderStatus newStatus) {
        return new Order(id, clientId, restaurantId, items, newStatus, total, createdAt);
    }
}
