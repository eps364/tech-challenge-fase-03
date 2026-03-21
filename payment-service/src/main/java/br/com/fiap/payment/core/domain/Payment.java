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
        validate(orderId, clientId, amount, attempts);
        this.id = id != null ? id : UUID.randomUUID();
        this.orderId = orderId;
        this.clientId = clientId;
        this.amount = amount;
        this.status = status != null ? status : PaymentStatus.PENDING;
        this.attempts = attempts;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
        this.updatedAt = updatedAt != null ? updatedAt : Instant.now();
    }

    private void validate(UUID orderId, UUID clientId, BigDecimal amount, int attempts) {
        if (orderId == null) throw new ValidationException("orderId", "The order id is required");
        if (clientId == null) throw new ValidationException("clientId", "The client id is required");
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("amount", "The amount must be greater than zero");
        }
        if (attempts < 0) {
            throw new ValidationException("attempts", "The attempts cannot be negative");
        }
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
        return new Payment(null, orderId, clientId, amount, null, 0, null, null);
    }
}
