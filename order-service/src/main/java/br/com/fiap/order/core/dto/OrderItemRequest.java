package br.com.fiap.order.core.dto;

public record OrderItemRequest(
        Long productId,
        int quantity
) {}
