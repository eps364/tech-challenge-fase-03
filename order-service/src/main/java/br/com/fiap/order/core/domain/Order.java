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

    public Order(UUID id,
                 UUID clientId,
                 UUID restaurantId,
                 List<OrderItem> items,
                 OrderStatus status,
                 BigDecimal total,
                 Instant createdAt) {
        validate(clientId, restaurantId, items, status, total, createdAt);
        this.id = id;
        this.clientId = clientId;
        this.restaurantId = restaurantId;
        this.items = List.copyOf(items);
        this.status = status;
        this.total = total;
        this.createdAt = createdAt;
    }

    public Order(UUID clientId,
                 UUID restaurantId,
                 List<OrderItem> items,
                 OrderStatus status,
                 BigDecimal total,
                 Instant createdAt) {
        this(null, clientId, restaurantId, items, status, total, createdAt);
    }

    public Order withStatus(OrderStatus newStatus) {
        return new Order(
                this.id,
                this.clientId,
                this.restaurantId,
                this.items,
                newStatus,
                this.total,
                this.createdAt
        );
    }

    private void validate(UUID clientId,
                          UUID restaurantId,
                          List<OrderItem> items,
                          OrderStatus status,
                          BigDecimal total,
                          Instant createdAt) {
        if (clientId == null) {
            throw new ValidationException("clientId", "The client id is required");
        }
        if (restaurantId == null) {
            throw new ValidationException("restaurantId", "The restaurant id is required");
        }
        if (items == null || items.isEmpty()) {
            throw new ValidationException("items", "The order items are required");
        }
        if (status == null) {
            throw new ValidationException("status", "The order status is required");
        }
        if (total == null || total.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("total", "The order total must be greater than zero");
        }
        if (createdAt == null) {
            throw new ValidationException("createdAt", "The createdAt is required");
        }
    }

    public UUID getId() {
        return id;
    }

    public UUID getClientId() {
        return clientId;
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}