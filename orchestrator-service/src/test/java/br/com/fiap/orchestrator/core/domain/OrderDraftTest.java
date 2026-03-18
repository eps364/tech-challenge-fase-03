package br.com.fiap.orchestrator.core.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class OrderDraftTest {

    @Test
    void constructor_shouldSetFieldsCorrectly() {
        UUID clientId = UUID.randomUUID();
        String cpf = "12345678900";
        UUID restaurantId = UUID.randomUUID();
        Address address = new Address("Rua A", "100", "Cidade", "Bairro", "Brasil", "SP", "12345-678");
        List<OrderItemDraft> items = List.of(
                new OrderItemDraft(UUID.randomUUID(), "Pizza", 2, BigDecimal.valueOf(20)),
                new OrderItemDraft(UUID.randomUUID(), "Burger", 1, BigDecimal.valueOf(15))
        );
        BigDecimal total = BigDecimal.valueOf(55);
        Instant createdAt = Instant.now();
        OrderDraft draft = new OrderDraft(clientId, cpf, restaurantId, items, address, total, createdAt);
        assertEquals(clientId, draft.getClientId());
        assertEquals(cpf, draft.getCpf());
        assertEquals(restaurantId, draft.getRestaurantId());
        assertEquals(items, draft.getItems());
        assertEquals(address, draft.getDeliveryAddress());
        assertEquals(total, draft.getTotal());
        assertEquals(createdAt, draft.getCreatedAt());
    }
}
