package br.com.fiap.payment.core.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class Payment {
    private final UUID id;
    private final UUID orderId;
    private final UUID clientId;
    private final BigDecimal amount;
    private final PaymentStatus status;
    private final int attempts;
    private final Instant createdAt;
    private final Instant updatedAt;

    public Payment(UUID id, UUID orderId, UUID clientId, BigDecimal amount,
                   PaymentStatus status, int attempts, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.orderId = orderId;
        this.clientId = clientId;
        this.amount = amount;
        this.status = status;
        this.attempts = attempts;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() { return id; }
    public UUID getOrderId() { return orderId; }
    public UUID getClientId() { return clientId; }
    public BigDecimal getAmount() { return amount; }
    public PaymentStatus getStatus() { return status; }
    public int getAttempts() { return attempts; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    public Payment incrementAttempts() {
        return new Payment(id, orderId, clientId, amount, status, attempts + 1, createdAt, Instant.now());
    }

    public Payment withStatus(PaymentStatus newStatus) {
        return new Payment(id, orderId, clientId, amount, newStatus, attempts, createdAt, Instant.now());
    }

    public static Payment newPending(UUID orderId, UUID clientId, BigDecimal amount) {
        Instant now = Instant.now();
        return new Payment(UUID.randomUUID(), orderId, clientId, amount, PaymentStatus.PENDING, 0, now, now);
    }
}
