package br.com.fiap.client.core.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import br.com.fiap.client.core.usecase.ValidationException;

class StateCodeTest {

    @Test
    void shouldNormalizeStateCodeToUpperCase() {
        StateCode stateCode = StateCode.of("sp");
        assertEquals("SP", stateCode.value());
    }

    @Test
    void shouldThrowWhenStateCodeIsInvalid() {
        assertThrows(ValidationException.class, () -> StateCode.of("Sao Paulo"));
    }
}
