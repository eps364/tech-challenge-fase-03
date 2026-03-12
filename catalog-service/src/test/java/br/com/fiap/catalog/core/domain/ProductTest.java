package br.com.fiap.catalog.core.domain;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void shouldCreateProductWithValidation() {
        Product product = Product.create("Burger", new BigDecimal("25.00"), UUID.randomUUID(), "Main");
        assertEquals("Burger", product.getName());
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        ValidationException ex = assertThrows(ValidationException.class, () -> 
            Product.create("", new BigDecimal("10.00"), UUID.randomUUID(), "Side")
        );
        assertEquals("name", ex.getField());
    }

    @Test
    void shouldThrowExceptionWhenPriceIsZeroOrLess() {
        ValidationException ex = assertThrows(ValidationException.class, () -> 
            Product.create("Burger", BigDecimal.ZERO, UUID.randomUUID(), "Main")
        );
        assertEquals("price", ex.getField());
    }

    @Test
    void shouldThrowExceptionWhenFoodTypeIsBlank() {
        ValidationException ex = assertThrows(ValidationException.class, () ->
            Product.create("Burger", BigDecimal.TEN, UUID.randomUUID(), " ")
        );
        assertEquals("foodType", ex.getField());
    }
}
