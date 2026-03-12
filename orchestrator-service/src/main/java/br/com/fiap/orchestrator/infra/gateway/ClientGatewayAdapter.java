package br.com.fiap.orchestrator.infra.gateway;

import br.com.fiap.orchestrator.core.dto.ClientResponse;
import br.com.fiap.orchestrator.core.gateway.ClientGateway;
import br.com.fiap.orchestrator.infra.client.ClientFeignClient;
import org.springframework.stereotype.Component;

@Component
public class ClientGatewayAdapter implements ClientGateway {

    private final ClientFeignClient clientFeignClient;

    public ClientGatewayAdapter(ClientFeignClient clientFeignClient) {
        this.clientFeignClient = clientFeignClient;
    }

    @Override
    public ClientResponse findById(String clientId) {
        return clientFeignClient.findById(clientId);
    }
}