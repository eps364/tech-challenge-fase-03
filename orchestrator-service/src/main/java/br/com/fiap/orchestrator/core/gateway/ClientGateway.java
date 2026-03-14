package br.com.fiap.orchestrator.core.gateway;

import br.com.fiap.orchestrator.core.domain.Client;

import java.util.UUID;

public interface ClientGateway {
    Client findById(UUID clientId);
}