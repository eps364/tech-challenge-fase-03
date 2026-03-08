package br.com.fiap.order.core.dto;

import br.com.fiap.order.core.domain.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        UUID clientId,
        UUID restaurantId,
        List<OrderItemResponse> items,
        OrderStatus status,
        BigDecimal total,
        Instant createdAt
) {}
