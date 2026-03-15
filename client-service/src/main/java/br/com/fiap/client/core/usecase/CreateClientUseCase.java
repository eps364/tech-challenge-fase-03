package br.com.fiap.client.core.usecase;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import br.com.fiap.client.core.domain.Address;
import br.com.fiap.client.core.domain.Client;
import br.com.fiap.client.core.dto.AddressRequest;
import br.com.fiap.client.core.dto.AddressResponse;
import br.com.fiap.client.core.dto.ClientRequest;
import br.com.fiap.client.core.dto.ClientResponse;
import br.com.fiap.client.core.gateway.ClientRepositoryPort;

public class CreateClientUseCase {

    private static final Logger logger = LoggerFactory.getLogger(CreateClientUseCase.class);

    private final ClientRepositoryPort repo;

    public CreateClientUseCase(ClientRepositoryPort repo) {
        this.repo = repo;
    }

    public ClientResponse execute(UUID targetId, UUID callerId, boolean isAdmin, ClientRequest request) {
        logger.info("Starting create client for targetId {} by caller {} (admin: {})", targetId, callerId, isAdmin);
        validateOwnership(targetId, callerId, isAdmin);
        logger.debug("Ownership validated for targetId {}", targetId);

        if (repo.existsByCpf(request.cpf())) {
            logger.warn("CPF {} already exists", request.cpf());
            throw new ClientConflictException("CPF already exists for another user");
        }

        if (repo.existsById(targetId)) {
            logger.warn("Client already exists for id: {}", targetId);
            throw new ClientConflictException("Client already exists for id: " + targetId);
        }

        Address address = toAddress(request.address());
        logger.debug("Address created for client: {}", address);
        Client client = new Client(targetId, request.cpf(), address, true);
        logger.debug("Client object created: {}", client);
        Client savedClient = repo.save(client);
        logger.info("Client saved successfully: {}", savedClient.getId());
        ClientResponse response = toResponse(savedClient);
        logger.debug("Response created: {}", response);
        return response;
    }

    private void validateOwnership(UUID targetId, UUID callerId, boolean isAdmin) {
        if (!isAdmin && !targetId.equals(callerId)) {
            logger.warn("Access denied: caller {} trying to create for {}", callerId, targetId);
            throw new ClientAccessDeniedException("User cannot create profile for another user id");
        }
    }

    private Address toAddress(AddressRequest request) {
        if (request == null) {
            logger.debug("Address request is null");
            return null;
        }

        Address address = new Address(
                UUID.randomUUID(),
                request.street(),
                request.number(),
                request.neighborhood(),
                request.complement(),
                request.city(),
                request.state(),
                request.zipCode());
        logger.debug("Address mapped: {}", address);
        return address;
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

        ClientResponse response = new ClientResponse(client.getId(), client.getCpf(), client.isActive(), addressResponse);
        logger.debug("Client response created: {}", response);
        return response;
    }
}
