//package br.com.fiap.payment.core.domain;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//import java.math.BigDecimal;
//import java.util.UUID;
//
//import org.junit.jupiter.api.Test;
//
//class PaymentTest {
//
//    @Test
//    void shouldCreatePendingPayment() {
//        Payment payment = Payment.newPending(UUID.randomUUID(), UUID.randomUUID(), new BigDecimal("100.00"));
//
//        assertNotNull(payment.getId());
//        assertEquals(PaymentStatus.PENDING, payment.getStatus());
//        assertEquals(0, payment.getAttempts());
//    }
//
//    @Test
//    void shouldThrowExceptionForInvalidAmount() {
//        ValidationException ex = assertThrows(ValidationException.class, () ->
//            Payment.newPending(UUID.randomUUID(), UUID.randomUUID(), BigDecimal.ZERO)
//        );
//        assertEquals("amount", ex.getField());
//    }
//
//    @Test
//    void shouldThrowWhenAttemptsIsNegative() {
//        ValidationException ex = assertThrows(ValidationException.class, () ->
//            new Payment(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), BigDecimal.ONE,
//                    PaymentStatus.PENDING, -1, null, null)
//        );
//        assertEquals("attempts", ex.getField());
//    }
//
//    @Test
//    void shouldIncrementAttempts() {
//        Payment payment = Payment.newPending(UUID.randomUUID(), UUID.randomUUID(), BigDecimal.TEN);
//        Payment updated = payment.incrementAttempts();
//
//        assertEquals(1, updated.getAttempts());
//    }
//}
