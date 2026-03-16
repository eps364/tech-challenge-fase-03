package br.com.fiap.payment.core.domain;

public enum ProcPagPaymentStatus {
    APPROVED,
    PENDING,
    FAILED;

    public static ProcPagPaymentStatus fromRaw(String rawStatus) {
        if (rawStatus == null || rawStatus.isBlank()) {
            return PENDING;
        }

        String normalized = rawStatus.trim().toLowerCase();

        return switch (normalized) {
            case "pago" -> APPROVED;
            default -> PENDING;
        };
    }
}