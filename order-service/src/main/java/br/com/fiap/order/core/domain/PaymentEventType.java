package br.com.fiap.order.core.domain;

import java.util.Arrays;
import java.util.Optional;

public enum PaymentEventType {
    PAYMENT_PENDING,
    PAYMENT_APPROVED,
    PAYMENT_FAILED;

    public static Optional<PaymentEventType> from(String rawType) {
        if (rawType == null || rawType.isBlank()) {
            return Optional.empty();
        }

        return Arrays.stream(values())
                .filter(type -> type.name().equalsIgnoreCase(rawType))
                .findFirst();
    }
}