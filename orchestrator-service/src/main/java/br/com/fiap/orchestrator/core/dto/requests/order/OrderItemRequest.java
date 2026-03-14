package br.com.fiap.orchestrator.core.dto.requests.order;

import jakarta.validation.ValidationException;

import java.math.BigDecimal;

public class OrderItemRequest {
    private final Long productId;
    private final String name;
    private final int quantity;
    private final BigDecimal price;
    private final BigDecimal subtotal;

    public OrderItemRequest(Long productId, String name, int quantity, BigDecimal price) {
        validate(productId, name, quantity, price);
        this.productId = productId;
        this.name = name.trim();
        this.quantity = quantity;
        this.price = price;
        this.subtotal = calculateSubtotal(price, quantity);
    }

    private void validate(Long productId, String name, int quantity, BigDecimal price) {
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

    public Long getProductId() { return productId; }
    public String getName()    { return name; }
    public int getQuantity()   { return quantity; }
    public BigDecimal getPrice()    { return price; }
    public BigDecimal getSubtotal() { return subtotal; }
}
