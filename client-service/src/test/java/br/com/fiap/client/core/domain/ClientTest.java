package br.com.fiap.client.core.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class ClientTest {

    @Test
    void shouldThrowAccessDeniedWhenCallerIsNotOwnerOrAdmin() {
        Client client = new Client(
                UUID.randomUUID(),
                "12345678909",
                new Address(UUID.randomUUID(), "Street", "10", "District", null, "City", "SP", "01001000"),
                true);

        AccessDeniedException ex = assertThrows(AccessDeniedException.class,
                () -> client.ensureCanBeManagedBy(UUID.randomUUID(), false, "User cannot update another user profile"));

        assertEquals("User cannot update another user profile", ex.getMessage());
    }

    @Test
    void shouldAllowWhenCallerIsAdmin() {
        Client client = new Client(
                UUID.randomUUID(),
                "12345678909",
                new Address(UUID.randomUUID(), "Street", "10", "District", null, "City", "SP", "01001000"),
                true);

        assertDoesNotThrow(() -> client.ensureCanBeManagedBy(UUID.randomUUID(), true, "Access denied"));
    }
}
