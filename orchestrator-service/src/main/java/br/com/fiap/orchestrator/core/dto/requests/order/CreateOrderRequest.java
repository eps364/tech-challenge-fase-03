package br.com.fiap.orchestrator.core.dto.requests.order;

import br.com.fiap.orchestrator.core.dto.requests.AddressRequest;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

public record CreateOrderRequest(
        String clientId,
        String cpf,
        String restaurantId,
        List<OrderItemRequest> items,
        AddressRequest address,
        BigInteger price,
        LocalDateTime requestDate
) {
}
    