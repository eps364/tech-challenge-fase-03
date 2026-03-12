package br.com.fiap.orchestrator.core.gateway;

import br.com.fiap.orchestrator.core.dto.ClientResponse;

public interface ClientGateway {
    ClientResponse findById(String clientId);
}