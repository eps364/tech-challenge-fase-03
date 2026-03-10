package br.com.fiap.order.core.domain;

import java.math.BigDecimal;

public class OrderItem {
    private final Long productId;
    private final String name;
    private final int quantity;
    private final BigDecimal price;
    private final BigDecimal subtotal;

    public OrderItem(Long productId, String name, int quantity, BigDecimal price) {
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.subtotal = price.multiply(BigDecimal.valueOf(quantity));
    }

    public Long getProductId() { return productId; }
    public String getName()    { return name; }
    public int getQuantity()   { return quantity; }
    public BigDecimal getPrice()    { return price; }
    public BigDecimal getSubtotal() { return subtotal; }
}
