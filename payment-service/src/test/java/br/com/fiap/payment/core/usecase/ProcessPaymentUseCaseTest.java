package br.com.fiap.payment.core.usecase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.fiap.payment.core.domain.Payment;
import br.com.fiap.payment.core.domain.PaymentStatus;
import br.com.fiap.payment.core.gateway.EventPublisherPort;
import br.com.fiap.payment.core.gateway.ExternalPaymentGatewayPort;
import br.com.fiap.payment.core.gateway.PaymentRepositoryPort;

class ProcessPaymentUseCaseTest {

    private InMemoryPaymentRepository repository;
    private TestEventPublisher eventPublisher;
    private TestExternalGateway externalGateway;
    private ProcessPaymentUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = new InMemoryPaymentRepository();
        eventPublisher = new TestEventPublisher();
        externalGateway = new TestExternalGateway();
        useCase = new ProcessPaymentUseCase(repository, externalGateway, eventPublisher, 3);
    }

    @Test
    void shouldApprovePaymentAndPublishApprovedEvent() {
        externalGateway.shouldApprove = true;

        useCase.execute(newPayload(), false);

        Payment saved = repository.single();
        assertEquals(PaymentStatus.APPROVED, saved.getStatus());
        assertEquals(1, saved.getAttempts());
        assertEquals(1, eventPublisher.approvedCount);
        assertEquals(0, eventPublisher.pendingCount);
    }

    @Test
    void shouldMarkAsPendingAndPublishPendingEventWhenGatewayFails() {
        externalGateway.shouldThrow = true;

        useCase.execute(newPayload(), false);

        Payment saved = repository.single();
        assertEquals(PaymentStatus.PENDING, saved.getStatus());
        assertEquals(1, saved.getAttempts());
        assertEquals(0, eventPublisher.approvedCount);
        assertEquals(1, eventPublisher.pendingCount);
    }

    @Test
    void shouldStopRequeueOnWorkerAfterMaxAttempts() {
        UUID orderId = UUID.fromString("7f4f5f6e-7f1a-45df-b3e8-e30f7ad4d95f");
        UUID clientId = UUID.fromString("f8d9fa58-a8ce-4e08-94bd-385bf5dc99dd");
        Payment existing = new Payment(
                UUID.randomUUID(),
                orderId,
                clientId,
                new BigDecimal("25.00"),
                PaymentStatus.PENDING,
                3,
                Instant.now(),
                Instant.now()
        );
        repository.save(existing);
        externalGateway.shouldThrow = true;

        useCase.execute(newPayload(orderId, clientId, "25.00"), true);

        Payment saved = repository.single();
        assertEquals(PaymentStatus.FAILED, saved.getStatus());
        assertTrue(saved.getAttempts() >= 4);
        assertEquals(0, eventPublisher.approvedCount);
        assertEquals(0, eventPublisher.pendingCount);
    }

    private Map<String, Object> newPayload() {
        return newPayload(
                UUID.fromString("4a697353-7a3f-4cd1-82a5-f64483f464f3"),
                UUID.fromString("ecf96e89-40c0-4f17-9b78-6f9bc4ee2e5d"),
                "100.50"
        );
    }

    private Map<String, Object> newPayload(UUID orderId, UUID clientId, String total) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("orderId", orderId.toString());
        payload.put("clientId", clientId.toString());
        payload.put("total", total);
        return payload;
    }

    private static class InMemoryPaymentRepository implements PaymentRepositoryPort {
        private Payment payment;

        @Override
        public Payment save(Payment payment) {
            this.payment = payment;
            return payment;
        }

        @Override
        public Optional<Payment> findByOrderId(UUID orderId) {
            if (payment == null || !payment.getOrderId().equals(orderId)) {
                return Optional.empty();
            }
            return Optional.of(payment);
        }

        Payment single() {
            return payment;
        }
    }

    private static class TestExternalGateway implements ExternalPaymentGatewayPort {
        boolean shouldApprove;
        boolean shouldThrow;

        @Override
        public boolean authorize(Payment payment) {
            if (shouldThrow) {
                throw new IllegalStateException("processor unavailable");
            }
            return shouldApprove;
        }
    }

    private static class TestEventPublisher implements EventPublisherPort {
        int approvedCount;
        int pendingCount;

        @Override
        public void publishPaymentApproved(Payment payment) {
            approvedCount++;
        }

        @Override
        public void publishPaymentPending(Payment payment, String reason) {
            pendingCount++;
        }
    }
}
