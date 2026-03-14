package br.com.fiap.orchestrator.core.domain;

import java.math.BigDecimal;
import java.util.UUID;

public class OrderItemDraft {
    private final UUID productId;
    private final String name;
    private final Integer quantity;
    private final BigDecimal price;
    private final BigDecimal subtotal;

    public OrderItemDraft(UUID productId, String name, Integer quantity, BigDecimal price) {
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.subtotal = price.multiply(BigDecimal.valueOf(quantity));
    }

    public UUID getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }
}