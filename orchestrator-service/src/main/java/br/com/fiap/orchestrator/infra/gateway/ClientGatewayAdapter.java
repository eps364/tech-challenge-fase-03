package br.com.fiap.orchestrator.infra.gateway;

import br.com.fiap.orchestrator.core.domain.Address;
import br.com.fiap.orchestrator.core.domain.Client;
import br.com.fiap.orchestrator.core.dto.responses.client.ClientResponse;
import br.com.fiap.orchestrator.core.gateway.ClientGateway;
import br.com.fiap.orchestrator.infra.client.ClientFeignClient;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ClientGatewayAdapter implements ClientGateway {

    private final ClientFeignClient clientFeignClient;

    public ClientGatewayAdapter(ClientFeignClient clientFeignClient) {
        this.clientFeignClient = clientFeignClient;
    }

    @Override
    public Client findById(UUID clientId) {
        ClientResponse response = clientFeignClient.getClientById(clientId);

        return new Client(
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
    }
}