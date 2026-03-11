package br.com.fiap.client.core.gateway;

import java.util.Optional;
import java.util.UUID;

import br.com.fiap.client.core.domain.Client;

public interface ClientRepositoryPort {
    Optional<Client> findById(UUID id);
    boolean existsById(UUID id);
    Client save(Client client);
    void deleteById(UUID id);
}