package br.com.fiap.orchestrator.core.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class OrderDraft {
    private final UUID id;
    private final UUID clientId;
    private final UUID restaurantId;
    private final List<OrderItemDraft> items;
    private final BigDecimal total;
    private final Instant createdAt;

    public OrderDraft(UUID id,
                      UUID clientId,
                      UUID restaurantId,
                      List<OrderItemDraft> items,
                      BigDecimal total,
                      Instant createdAt) {
        this.id = id;
        this.clientId = clientId;
        this.restaurantId = restaurantId;
        this.items = items;
        this.total = total;
        this.createdAt = createdAt;
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

    public List<OrderItemDraft> getItems() {
        return items;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}