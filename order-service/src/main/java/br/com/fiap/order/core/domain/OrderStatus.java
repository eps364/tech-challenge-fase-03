package br.com.fiap.order.core.domain;

public enum OrderStatus {
    PENDING_CONFIRMATION,
    CONFIRMED,
    PENDING_PAYMENT,
    PAID,
    CANCELLED
}
