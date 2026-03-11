package br.com.fiap.client.core.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import br.com.fiap.client.core.usecase.ValidationException;

class ZipCodeTest {

    @Test
    void shouldNormalizeZipCode() {
        ZipCode zipCode = ZipCode.of("01001-000");
        assertEquals("01001000", zipCode.value());
    }

    @Test
    void shouldThrowWhenZipCodeIsInvalid() {
        assertThrows(ValidationException.class, () -> ZipCode.of("abc"));
    }
}
