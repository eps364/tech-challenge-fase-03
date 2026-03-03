package br.com.fiap.client.infra.cliente;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import br.com.fiap.client.core.domain.Cliente;
import br.com.fiap.client.core.gateway.ClientesRepositoryPort;

@Repository
public class ClientesRepositoryAdapter implements ClientesRepositoryPort {

    private final Map<UUID, Cliente> store = new ConcurrentHashMap<>();

    @Override
    public Optional<Cliente> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Cliente save(Cliente cliente) {
        store.put(cliente.getId(), cliente);
        return cliente;
    }
}