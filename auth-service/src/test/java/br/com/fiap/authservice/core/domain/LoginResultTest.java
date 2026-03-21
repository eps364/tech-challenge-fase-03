package br.com.fiap.authservice.core.domain;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

class LoginResultTest {

    @Test
    void shouldThrowWhenAccessTokenIsBlank() {
        ValidationException ex = assertThrows(ValidationException.class,
                () -> new LoginResult(UUID.randomUUID(), " ", "refresh", 300L, 600L, "Bearer", List.of("ROLE_USER")));

        assertEquals("accessToken", ex.getField());
    }

    @Test
    void shouldThrowWhenExpiresInIsInvalid() {
        ValidationException ex = assertThrows(ValidationException.class,
                () -> new LoginResult(UUID.randomUUID(), "access", "refresh", 0L, 600L, "Bearer", List.of("ROLE_USER")));

        assertEquals("expiresIn", ex.getField());
    }
}
