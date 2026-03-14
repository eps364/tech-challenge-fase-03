package br.com.fiap.order.core.dto.requests.order;

import java.math.BigInteger;
import java.time.LocalDateTime;

public record CreateOrderRequest(
        String clientId,
        String cpf,
        String restaurantId,
        String foodId,
        Integer quantity,
        AddressRequest address,
        BigInteger price,
        LocalDateTime requestDate
) {
}