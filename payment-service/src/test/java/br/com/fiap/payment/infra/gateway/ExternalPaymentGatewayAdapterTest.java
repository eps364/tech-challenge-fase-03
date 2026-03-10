package br.com.fiap.payment.infra.gateway;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.payment.core.domain.Payment;
import br.com.fiap.payment.core.domain.PaymentStatus;

@ExtendWith(MockitoExtension.class)
class ExternalPaymentGatewayAdapterTest {

    @Mock
    private ProcPagFeignClient procPagFeignClient;

    private ExternalPaymentGatewayAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new ExternalPaymentGatewayAdapter(procPagFeignClient);
    }

    @Test
    void shouldApproveWhenStatusIsAcceptedLowercase() {
        Payment payment = newPayment();
        mockStatus(payment.getId(), "accepted");

        boolean approved = adapter.authorize(payment);

        assertTrue(approved);
        verify(procPagFeignClient).requestPayment(any());
        verify(procPagFeignClient).getPaymentStatus(eq(payment.getId()));
    }

    @Test
    void shouldApproveWhenStatusIsAcceptedUppercase() {
        Payment payment = newPayment();
        mockStatus(payment.getId(), "ACCEPTED");

        boolean approved = adapter.authorize(payment);

        assertTrue(approved);
    }

    @Test
    void shouldNotApproveWhenStatusIsApproved() {
        Payment payment = newPayment();
        mockStatus(payment.getId(), "approved");

        boolean approved = adapter.authorize(payment);

        assertFalse(approved);
    }

    @Test
    void shouldNotApproveWhenStatusIsAuthorized() {
        Payment payment = newPayment();
        mockStatus(payment.getId(), "authorized");

        boolean approved = adapter.authorize(payment);

        assertFalse(approved);
    }

    @Test
    void shouldNotApproveWhenStatusIsNull() {
        Payment payment = newPayment();
        when(procPagFeignClient.requestPayment(any())).thenReturn(Map.of("requestId", payment.getId().toString()));
        when(procPagFeignClient.getPaymentStatus(eq(payment.getId()))).thenReturn(Map.of());

        boolean approved = adapter.authorize(payment);

        assertFalse(approved);
    }

    private void mockStatus(UUID paymentId, String status) {
        when(procPagFeignClient.requestPayment(any())).thenReturn(Map.of("requestId", paymentId.toString()));
        when(procPagFeignClient.getPaymentStatus(eq(paymentId))).thenReturn(Map.of("status", status));
    }

    private Payment newPayment() {
        return new Payment(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                new BigDecimal("100.00"),
                PaymentStatus.PENDING,
                0,
                Instant.now(),
                Instant.now()
        );
    }
}
