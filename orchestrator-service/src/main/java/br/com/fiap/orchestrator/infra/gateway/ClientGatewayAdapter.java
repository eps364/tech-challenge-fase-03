package br.com.fiap.orchestrator.infra.gateway;

import br.com.fiap.orchestrator.core.domain.Address;
import br.com.fiap.orchestrator.core.domain.Client;
import br.com.fiap.orchestrator.core.dto.responses.client.ClientResponse;
import br.com.fiap.orchestrator.core.gateway.ClientGateway;
import br.com.fiap.orchestrator.infra.client.ClientFeignClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ClientGatewayAdapter implements ClientGateway {

    private static final Logger logger = LoggerFactory.getLogger(ClientGatewayAdapter.class);

    private final ClientFeignClient clientFeignClient;

    public ClientGatewayAdapter(ClientFeignClient clientFeignClient) {
        this.clientFeignClient = clientFeignClient;
    }

    @Override
    public Client findById(UUID clientId) {
        logger.info("Calling client service to find client by ID: {}", clientId);
        try {
            ClientResponse response = clientFeignClient.getClientById(clientId);
            logger.info("Received client response for ID {}: {}", clientId, response.id());

            Client client = new Client(
                    response.id(),
                    response.cpf(),
                    new Address(
                            response.address().street(),
                            response.address().number(),
                            response.address().city(),
                            response.address().neighborhood(),
                            response.address().complement(),
                            response.address().state(),
                            response.address().zipCode()
                    )
            );
            logger.debug("Mapped client: {}", client);
            return client;
        } catch (Exception e) {
            logger.error("Error calling client service for ID {}: {}", clientId, e.getMessage(), e);
            throw e;
        }
    }
}