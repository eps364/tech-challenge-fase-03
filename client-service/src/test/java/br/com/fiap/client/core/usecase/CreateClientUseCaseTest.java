package br.com.fiap.client.core.usecase;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.Test;

import br.com.fiap.client.core.domain.Client;
import br.com.fiap.client.core.dto.AddressRequest;
import br.com.fiap.client.core.dto.ClientRequest;
import br.com.fiap.client.core.gateway.ClientRepositoryPort;

class CreateClientUseCaseTest {

    @Test
    void shouldFailWhenCpfAlreadyExists() {
        InMemoryClientRepository repository = new InMemoryClientRepository();
        CreateClientUseCase useCase = new CreateClientUseCase(repository);

        UUID userId = UUID.randomUUID();
        repository.save(new Client(
                userId,
                "12345678901",
                new br.com.fiap.client.core.domain.Address(
                        UUID.randomUUID(), "Street", "10", "District", null, "City", "SP", "01001000"),
                true));

        ClientRequest request = new ClientRequest(
                "12345678901",
                new AddressRequest("Street 2", "20", "District 2", null, "City", "RJ", "22041001"));

        assertThrows(ClientConflictException.class, () -> useCase.execute(UUID.randomUUID(), UUID.randomUUID(), true, request));
    }

    private static class InMemoryClientRepository implements ClientRepositoryPort {
        private final Map<UUID, Client> storage = new ConcurrentHashMap<>();

        @Override
        public Optional<Client> findById(UUID id) {
            return Optional.ofNullable(storage.get(id));
        }

        @Override
        public boolean existsById(UUID id) {
            return storage.containsKey(id);
        }

        @Override
        public boolean existsByCpf(String cpf) {
            return storage.values().stream().anyMatch(client -> client.getCpf().equals(cpf));
        }

        @Override
        public boolean existsByCpfAndIdNot(String cpf, UUID id) {
            return storage.values().stream()
                    .anyMatch(client -> client.getCpf().equals(cpf) && !client.getId().equals(id));
        }

        @Override
        public Client save(Client client) {
            storage.put(client.getId(), client);
            return client;
        }

        @Override
        public void deleteById(UUID id) {
            storage.remove(id);
        }
    }
}
