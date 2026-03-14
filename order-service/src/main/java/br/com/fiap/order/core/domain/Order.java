package br.com.fiap.order.core.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
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
        validate(clientId, restaurantId, items, status, total);
        this.id = id != null ? id : UUID.randomUUID();
        this.clientId = clientId;
        this.restaurantId = restaurantId;
        this.items = Collections.unmodifiableList(List.copyOf(items));
        this.status = status != null ? status : OrderStatus.PENDING_PAYMENT;
        this.total = total != null ? total : calculateTotal(items);
        this.createdAt = createdAt != null ? createdAt : Instant.now();
    }

    public static Order create(UUID clientId, UUID restaurantId, List<OrderItem> items) {
        return new Order(null, clientId, restaurantId, items, null, null, null);
    }

    private void validate(UUID clientId, UUID restaurantId, List<OrderItem> items, OrderStatus status, BigDecimal total) {
        if (clientId == null) {
            throw new ValidationException("clientId", "The client id is required");
        }
        if (restaurantId == null) {
            throw new ValidationException("restaurantId", "The restaurant id is required");
        }
        if (items == null || items.isEmpty()) {
            throw new ValidationException("items", "The order must have at least one item");
        }
        if (items.stream().anyMatch(java.util.Objects::isNull)) {
            throw new ValidationException("items", "The order items cannot contain null entries");
        }
        if (total != null && total.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("total", "The total must be greater than zero");
        }
    }

    private BigDecimal calculateTotal(List<OrderItem> items) {
        return items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
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

    public Order withStatus(OrderStatus newStatus) {
        return new Order(id, clientId, restaurantId, items, newStatus, total, createdAt);
    }

    public Order markAsPaid() {
        if (this.status == OrderStatus.PAID) {
            return this;
        }
        return withStatus(OrderStatus.PAID);
    }
}
