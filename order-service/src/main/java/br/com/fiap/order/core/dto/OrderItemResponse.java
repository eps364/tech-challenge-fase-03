package br.com.fiap.order.core.dto;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long productId,
        String name,
        int quantity,
        BigDecimal price,
        BigDecimal subtotal
) {}
