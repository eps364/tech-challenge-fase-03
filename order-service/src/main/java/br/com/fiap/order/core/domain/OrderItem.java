package br.com.fiap.order.core.domain;

import java.math.BigDecimal;
import java.util.UUID;

public class OrderItem {
    private final UUID productId;
    private final String name;
    private final int quantity;
    private final BigDecimal price;
    private final BigDecimal subtotal;

    public OrderItem(UUID productId, String name, int quantity, BigDecimal price) {
        validate(productId, name, quantity, price);
        this.productId = productId;
        this.name = name.trim();
        this.quantity = quantity;
        this.price = price;
        this.subtotal = calculateSubtotal(price, quantity);
    }

    private void validate(UUID productId, String name, int quantity, BigDecimal price) {
        if (productId == null) {
            throw new ValidationException("productId", "The product id is required");
        }
        if (name == null || name.isBlank()) {
            throw new ValidationException("name", "The name is required");
        }
        if (quantity <= 0) {
            throw new ValidationException("quantity", "The quantity must be greater than zero");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("price", "The price must be greater than zero");
        }
    }

    private BigDecimal calculateSubtotal(BigDecimal price, int quantity) {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    public UUID getProductId() { return productId; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public BigDecimal getPrice() { return price; }
    public BigDecimal getSubtotal() { return subtotal; }
}
