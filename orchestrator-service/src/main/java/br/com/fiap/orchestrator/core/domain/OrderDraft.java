package br.com.fiap.orchestrator.core.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class OrderDraft {
    private final UUID clientId;
    private final String cpf;
    private final UUID restaurantId;
    private final List<OrderItemDraft> items;
    private final Address deliveryAddress;
    private final BigDecimal total;
    private final Instant createdAt;

    public OrderDraft(UUID clientId,
                      String cpf,
                      UUID restaurantId,
                      List<OrderItemDraft> items,
                      Address deliveryAddress,
                      BigDecimal total,
                      Instant createdAt) {
        this.clientId = clientId;
        this.cpf = cpf;
        this.restaurantId = restaurantId;
        this.items = items;
        this.deliveryAddress = deliveryAddress;
        this.total = total;
        this.createdAt = createdAt;
    }

    public UUID getClientId() {
        return clientId;
    }

    public String getCpf() {
        return cpf;
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public List<OrderItemDraft> getItems() {
        return items;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
