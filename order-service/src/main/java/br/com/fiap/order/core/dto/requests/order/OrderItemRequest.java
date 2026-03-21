package br.com.fiap.order.core.dto.requests.order;

public record OrderItemRequest(
        Long productId,
        int quantity
) {}
