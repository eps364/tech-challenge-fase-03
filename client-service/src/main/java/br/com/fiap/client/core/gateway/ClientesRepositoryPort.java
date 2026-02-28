package br.com.fiap.client.core.gateway;

import br.com.fiap.client.core.domain.Cliente;

import java.util.Optional;
import java.util.UUID;

public interface ClientesRepositoryPort {
    Optional<Cliente> findById(UUID id);
    Cliente save(Cliente cliente);
}