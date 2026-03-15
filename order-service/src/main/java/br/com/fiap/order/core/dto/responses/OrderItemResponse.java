package br.com.fiap.order.core.dto.responses;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponse(
        UUID productId,
        String name,
        Integer quantity,
        BigDecimal price,
        BigDecimal subtotal
) {
}
