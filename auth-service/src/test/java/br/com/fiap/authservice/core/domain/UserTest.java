package br.com.fiap.authservice.core.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void shouldCreateUserWithCalculatedId() {
        User user = User.create("jdoe", "jdoe@example.com", "John", "Doe");
        assertNotNull(user.getId());
        assertEquals("jdoe", user.getUsername());
    }

    @Test
    void shouldThrowExceptionWhenUsernameIsBlank() {
        ValidationException ex = assertThrows(ValidationException.class, () -> 
            User.create("", "jdoe@example.com", "John", "Doe")
        );
        assertEquals("username", ex.getField());
    }

    @Test
    void shouldCreateUserWithRoles() {
        User user = new User(UUID.randomUUID(), "jdoe", "jdoe@example.com", "John", "Doe", List.of("ROLE_USER"));
        assertTrue(user.getRoles().contains("ROLE_USER"));
    }

    @Test
    void shouldThrowExceptionWhenEmailFormatIsInvalid() {
        ValidationException ex = assertThrows(ValidationException.class, () ->
            User.create("jdoe", "invalid-email", "John", "Doe")
        );
        assertEquals("email", ex.getField());
    }

    @Test
    void shouldThrowExceptionWhenUsernameFormatIsInvalid() {
        ValidationException ex = assertThrows(ValidationException.class, () ->
            User.create("jd", "jdoe@example.com", "John", "Doe")
        );
        assertEquals("username", ex.getField());
    }
}
