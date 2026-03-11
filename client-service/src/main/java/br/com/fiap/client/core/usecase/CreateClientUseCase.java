package br.com.fiap.client.core.usecase;

import java.util.UUID;

import br.com.fiap.client.core.domain.Address;
import br.com.fiap.client.core.domain.Client;
import br.com.fiap.client.core.dto.AddressRequest;
import br.com.fiap.client.core.dto.AddressResponse;
import br.com.fiap.client.core.dto.ClientRequest;
import br.com.fiap.client.core.dto.ClientResponse;
import br.com.fiap.client.core.gateway.ClientRepositoryPort;

public class CreateClientUseCase {

    private final ClientRepositoryPort repo;

    public CreateClientUseCase(ClientRepositoryPort repo) {
        this.repo = repo;
    }

    public ClientResponse execute(UUID targetId, UUID callerId, boolean isAdmin, ClientRequest request) {
        validateOwnership(targetId, callerId, isAdmin);

        if (repo.existsByCpf(request.cpf())) {
            throw new ClientConflictException("CPF already exists for another user");
        }

        if (repo.existsById(targetId)) {
            throw new ClientConflictException("Client already exists for id: " + targetId);
        }

        Address address = toAddress(request.address());
        Client client = new Client(targetId, request.cpf(), address, true);
        Client savedClient = repo.save(client);
        return toResponse(savedClient);
    }

    private void validateOwnership(UUID targetId, UUID callerId, boolean isAdmin) {
        if (!isAdmin && !targetId.equals(callerId)) {
            throw new ClientAccessDeniedException("User cannot create profile for another user id");
        }
    }

    private Address toAddress(AddressRequest request) {
        if (request == null) {
            return null;
        }

        return new Address(
                UUID.randomUUID(),
                request.street(),
                request.number(),
                request.neighborhood(),
                request.complement(),
                request.city(),
                request.state(),
                request.zipCode());
    }

    private ClientResponse toResponse(Client client) {
        Address address = client.getAddress();
        AddressResponse addressResponse = address == null
                ? null
                : new AddressResponse(
                        address.getId(),
                        address.getStreet(),
                        address.getNumber(),
                        address.getNeighborhood(),
                        address.getComplement(),
                        address.getCity(),
                        address.getState(),
                        address.getZipCode());

        return new ClientResponse(client.getId(), client.getCpf(), client.isActive(), addressResponse);
    }
}
