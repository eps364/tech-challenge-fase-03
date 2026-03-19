package br.com.fiap.orchestrator.core.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class OrderItemDraftTest {

    @Test
    void constructor_shouldCalculateSubtotalCorrectly() {
        UUID productId = UUID.randomUUID();
        String name = "Pizza";
        int quantity = 3;
        BigDecimal price = BigDecimal.valueOf(15);
        OrderItemDraft item = new OrderItemDraft(productId, name, quantity, price);
        assertEquals(BigDecimal.valueOf(45), item.getSubtotal());
    }

    @Test
    void getters_shouldReturnCorrectValues() {
        UUID productId = UUID.randomUUID();
        String name = "Burger";
        int quantity = 2;
        BigDecimal price = BigDecimal.valueOf(20);
        OrderItemDraft item = new OrderItemDraft(productId, name, quantity, price);
        assertEquals(productId, item.getProductId());
        assertEquals(name, item.getName());
        assertEquals(quantity, item.getQuantity());
        assertEquals(price, item.getPrice());
    }
}
