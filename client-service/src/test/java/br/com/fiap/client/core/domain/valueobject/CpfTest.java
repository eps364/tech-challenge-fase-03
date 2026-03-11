package br.com.fiap.client.core.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import br.com.fiap.client.core.domain.ValidationException;

class CpfTest {

    @Test
    void shouldCreateCpfWhenValid() {
        Cpf cpf = Cpf.of("12345678909");
        assertEquals("12345678909", cpf.value());
    }

    @Test
    void shouldThrowWhenCpfIsInvalid() {
        assertThrows(ValidationException.class, () -> Cpf.of("123"));
    }

    @Test
    void shouldThrowWhenCpfChecksumIsInvalid() {
        assertThrows(ValidationException.class, () -> Cpf.of("12345678901"));
    }
}
