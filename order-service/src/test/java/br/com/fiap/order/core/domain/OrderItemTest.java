//package br.com.fiap.order.core.domain;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//import java.math.BigDecimal;
//
//import org.junit.jupiter.api.Test;
//
//class OrderItemTest {
//
//    @Test
//    void shouldCalculateSubtotal() {
//        OrderItem item = new OrderItem(1L, "Burger", 2, new BigDecimal("12.50"));
//
//        assertEquals(new BigDecimal("25.00"), item.getSubtotal());
//    }
//
//    @Test
//    void shouldThrowWhenPriceIsInvalid() {
//        ValidationException ex = assertThrows(ValidationException.class,
//                () -> new OrderItem(1L, "Burger", 1, BigDecimal.ZERO));
//
//        assertEquals("price", ex.getField());
//    }
//
//    @Test
//    void shouldThrowWhenNameIsBlank() {
//        ValidationException ex = assertThrows(ValidationException.class,
//                () -> new OrderItem(1L, " ", 1, BigDecimal.ONE));
//
//        assertEquals("name", ex.getField());
//    }
//}
