package br.com.fiap.order.core.domain;

import java.util.Arrays;
import java.util.Optional;

public enum PaymentEventType {
    PAYMENT_APPROVED,
    PAYMENT_PENDING;

    public static Optional<PaymentEventType> from(String rawType) {
        if (rawType == null || rawType.isBlank()) {
            return Optional.empty();
        }

        return Arrays.stream(values())
                .filter(type -> type.name().equalsIgnoreCase(rawType))
                .findFirst();
    }
}