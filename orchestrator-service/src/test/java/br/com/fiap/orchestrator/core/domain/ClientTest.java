package br.com.fiap.orchestrator.core.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class ClientTest {

    @Test
    void constructor_and_getters_shouldWork() {
        UUID id = UUID.randomUUID();
        String cpf = "98765432100";
        Address address = new Address("Rua B", "200", "Cidade", "Bairro", "Brasil", "RJ", "23456-789");
        Client client = new Client(id, cpf, address);
        assertEquals(id, client.getId());
        assertEquals(cpf, client.getCpf());
        assertEquals(address, client.getAddress());
    }
}
