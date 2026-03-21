package br.com.fiap.client.core.usecase;

import java.util.UUID;

import br.com.fiap.client.core.domain.Address;
import br.com.fiap.client.core.domain.Client;
import br.com.fiap.client.core.dto.AddressResponse;
import br.com.fiap.client.core.dto.ClientResponse;
import br.com.fiap.client.core.gateway.ClientRepositoryPort;

public class GetClientUseCase {

    private final ClientRepositoryPort repo;

    public GetClientUseCase(ClientRepositoryPort repo) {
        this.repo = repo;
    }

    public ClientResponse execute(UUID targetId, UUID callerId, boolean isAdmin) {
        validateOwnership(targetId, callerId, isAdmin);

        Client client = repo.findById(targetId)
                .orElseThrow(() -> new ClientNotFoundException("Client not found: " + targetId));

        return toResponse(client);
    }

    private void validateOwnership(UUID targetId, UUID callerId, boolean isAdmin) {
        if (!isAdmin && !targetId.equals(callerId)) {
            throw new ClientAccessDeniedException("User cannot access another user profile");
        }
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
