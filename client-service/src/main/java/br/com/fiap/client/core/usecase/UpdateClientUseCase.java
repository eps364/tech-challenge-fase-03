package br.com.fiap.client.core.usecase;

import java.util.UUID;

import br.com.fiap.client.core.domain.Address;
import br.com.fiap.client.core.domain.Client;
import br.com.fiap.client.core.dto.AddressResponse;
import br.com.fiap.client.core.dto.ClientRequest;
import br.com.fiap.client.core.dto.ClientResponse;
import br.com.fiap.client.core.gateway.ClientRepositoryPort;

public class UpdateClientUseCase {

    private final ClientRepositoryPort repo;

    public UpdateClientUseCase(ClientRepositoryPort repo) {
        this.repo = repo;
    }

    public ClientResponse execute(UUID targetId, UUID callerId, boolean isAdmin, ClientRequest request) {
        Client current = repo.findById(targetId)
                .orElseThrow(() -> new ClientNotFoundException("Client not found: " + targetId));

        current.ensureCanBeManagedBy(callerId, isAdmin, "User cannot update another user profile");

        if (repo.existsByCpfAndIdNot(request.cpf(), targetId)) {
            throw new ClientConflictException("CPF already exists for another user");
        }

        Address currentAddress = current.getAddress();
        UUID addressId = currentAddress == null ? UUID.randomUUID() : currentAddress.getId();

        Address newAddress = request.address() == null
                ? null
                : new Address(
                        addressId,
                        request.address().street(),
                        request.address().number(),
                        request.address().neighborhood(),
                        request.address().complement(),
                        request.address().city(),
                        request.address().state(),
                        request.address().zipCode());

        Client updated = current.withProfile(request.cpf(), newAddress);
        Client saved = repo.save(updated);
        return toResponse(saved);
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
