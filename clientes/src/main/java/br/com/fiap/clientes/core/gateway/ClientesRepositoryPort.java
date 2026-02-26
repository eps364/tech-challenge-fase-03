package br.com.fiap.clientes.core.gateway;

import br.com.fiap.clientes.core.domain.Cliente;

import java.util.Optional;
import java.util.UUID;

public interface ClientesRepositoryPort {
    Optional<Cliente> findById(UUID id);
    Cliente save(Cliente cliente);
}