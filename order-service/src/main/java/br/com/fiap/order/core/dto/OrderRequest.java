package br.com.fiap.order.core.dto;

import java.util.List;
import java.util.UUID;

public record OrderRequest(
        UUID restaurantId,
        List<OrderItemRequest> items
) {}
