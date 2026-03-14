package br.com.fiap.orchestrator.core.dto.requests.order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record CreateOrderRequest(
        UUID clientId,
        String cpf,
        UUID restaurantId,
        List<OrderItemRequest> items,
        AddressRequest address,
        BigDecimal price,
        Instant requestDate
) {
}
