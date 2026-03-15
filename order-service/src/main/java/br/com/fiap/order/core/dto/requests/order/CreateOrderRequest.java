package br.com.fiap.order.core.dto.requests.order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record CreateOrderRequest(
        UUID clientId,
        String cpf,
        UUID restaurantId,
        List<CreateOrderItemRequest> items,
        AddressRequest address,
        BigDecimal price,
        Instant requestDate
) {
}