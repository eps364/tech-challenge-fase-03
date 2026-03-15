package br.com.fiap.orchestrator.core.dto.requests.order;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemRequest(
        UUID productId,
        String name,
        Integer quantity,
        BigDecimal price,
        BigDecimal subtotal
) {
}
