package br.com.fiap.order.core.dto.requests.order;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateOrderItemRequest(
        UUID productId,
        String name,
        Integer quantity,
        BigDecimal price,
        BigDecimal subtotal
) {
}
