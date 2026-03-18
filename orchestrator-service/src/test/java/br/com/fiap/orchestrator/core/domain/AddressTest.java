package br.com.fiap.orchestrator.core.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AddressTest {

    @Test
    void constructor_and_getters_shouldWork() {
        Address address = new Address("Rua C", "300", "Cidade", "Bairro", "Brasil", "MG", "34567-890");
        assertEquals("Rua C", address.getStreet());
        assertEquals("300", address.getNumber());
        assertEquals("Cidade", address.getCity());
        assertEquals("Bairro", address.getNeighborhood());
        assertEquals("Brasil", address.getCountry());
        assertEquals("MG", address.getState());
        assertEquals("34567-890", address.getZipCode());
    }
}
