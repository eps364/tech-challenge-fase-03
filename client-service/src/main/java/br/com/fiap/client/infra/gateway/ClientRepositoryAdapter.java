package br.com.fiap.client.infra.gateway;

import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import br.com.fiap.client.core.domain.Address;
import br.com.fiap.client.core.domain.Client;
import br.com.fiap.client.core.gateway.ClientRepositoryPort;
import br.com.fiap.client.core.usecase.ClientConflictException;
import br.com.fiap.client.infra.entity.ClientEntity;
import br.com.fiap.client.infra.repository.ClientJpaRepository;

@Repository
public class ClientRepositoryAdapter implements ClientRepositoryPort {

    private final ClientJpaRepository repo;

    public ClientRepositoryAdapter(ClientJpaRepository repo) {
        this.repo = repo;
    }

    @Override
    public Optional<Client> findById(UUID id) {
        return repo.findById(id).map(this::toDomain);
    }

    @Override
    public boolean existsById(UUID id) {
        return repo.existsById(id);
    }

    @Override
    public boolean existsByCpf(String cpf) {
        return repo.existsByCpf(cpf);
    }

    @Override
    public boolean existsByCpfAndIdNot(String cpf, UUID id) {
        return repo.existsByCpfAndIdNot(cpf, id);
    }

    @Override
    public Client save(Client client) {
        try {
            ClientEntity saved = repo.save(toEntity(client));
            return toDomain(saved);
        } catch (DataIntegrityViolationException ex) {
            throw new ClientConflictException(resolveConflictMessage(ex));
        }
    }

    private String resolveConflictMessage(DataIntegrityViolationException ex) {
        String message = ex.getMostSpecificCause() != null
                ? ex.getMostSpecificCause().getMessage()
                : ex.getMessage();

        if (message != null && message.contains("clients_cpf_key")) {
            return "CPF already exists for another user";
        }

        return "Address already exists for this user";
    }

    @Override
    public void deleteById(UUID id) {
        repo.deleteById(id);
    }

    private Client toDomain(ClientEntity entity) {
        Address address = new Address(
                entity.getAddressId(),
                entity.getStreet(),
                entity.getNumber(),
                entity.getNeighborhood(),
                entity.getComplement(),
                entity.getCity(),
                entity.getState(),
                entity.getZipCode());
        return new Client(entity.getId(), entity.getCpf(), address, entity.isActive());
    }

    private ClientEntity toEntity(Client domain) {
        Address address = domain.getAddress();
        UUID addressId = address == null ? null : address.getId();
        return new ClientEntity(
                domain.getId(),
                domain.getCpf(),
                domain.isActive(),
                addressId,
                address == null ? null : address.getStreet(),
                address == null ? null : address.getNumber(),
                address == null ? null : address.getNeighborhood(),
                address == null ? null : address.getComplement(),
                address == null ? null : address.getCity(),
                address == null ? null : address.getState(),
                address == null ? null : address.getZipCode());
    }
}