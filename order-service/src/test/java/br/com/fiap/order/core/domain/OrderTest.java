//package br.com.fiap.order.core.domain;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.UUID;
//
//import org.junit.jupiter.api.Test;
//
//class OrderTest {
//
//    @Test
//    void shouldCreateOrderWithCalculatedTotal() {
//        OrderItem item1 = new OrderItem(1L, "Prod 1", 2, new BigDecimal("10.00"));
//        OrderItem item2 = new OrderItem(2L, "Prod 2", 1, new BigDecimal("5.00"));
//
//        Order order = Order.create(UUID.randomUUID(), UUID.randomUUID(), List.of(item1, item2));
//
//        assertEquals(new BigDecimal("25.00"), order.getTotal());
//        assertEquals(OrderStatus.PENDING_PAYMENT, order.getStatus());
//    }
//
//    @Test
//    void shouldThrowExceptionWhenItemsAreEmpty() {
//        ValidationException ex = assertThrows(ValidationException.class, () ->
//            Order.create(UUID.randomUUID(), UUID.randomUUID(), List.of())
//        );
//        assertEquals("items", ex.getField());
//    }
//
//    @Test
//    void shouldThrowExceptionWhenItemsContainNull() {
//        ValidationException ex = assertThrows(ValidationException.class, () ->
//            Order.create(UUID.randomUUID(), UUID.randomUUID(), java.util.Arrays.asList(new OrderItem(1L, "P1", 1, BigDecimal.TEN), null))
//        );
//        assertEquals("items", ex.getField());
//    }
//
//    @Test
//    void shouldMarkAsPaid() {
//        Order order = Order.create(UUID.randomUUID(), UUID.randomUUID(),
//            List.of(new OrderItem(1L, "P1", 1, BigDecimal.TEN)));
//
//        Order paidOrder = order.markAsPaid();
//
//        assertEquals(OrderStatus.PAID, paidOrder.getStatus());
//    }
//}
