package br.com.fiap.client.core.usecase;

import java.util.Map;
import java.util.UUID;

import br.com.fiap.client.core.domain.Client;
import br.com.fiap.client.core.gateway.ClientRepositoryPort;

public class ProcessOrchestratorRequestUseCase {

    private final ClientRepositoryPort repo;

    public ProcessOrchestratorRequestUseCase(ClientRepositoryPort repo) {
        this.repo = repo;
    }

    public ProcessOrchestratorRequestResult execute(ProcessOrchestratorRequestCommand cmd) {

        if (isClientActivate(cmd.type())) {
            UUID clientId = resolveClientId(cmd.payload());

            Client client = repo.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client not found: " + clientId));

            Client updatedClient = client.activate();
            repo.save(updatedClient);

            return new ProcessOrchestratorRequestResult(
                    cmd.correlationId(),
                "CLIENT_ACTIVATED",
                    Map.of(
                    "clientId", updatedClient.getId().toString(),
                        "active", updatedClient.isActive(),
                    "cpf", updatedClient.getCpf()
                    )
            );
        }

        return new ProcessOrchestratorRequestResult(
                cmd.correlationId(),
            "CLIENTS_ACK",
            Map.of("message", "Unhandled type: " + cmd.type())
        );
    }

    private boolean isClientActivate(String type) {
        return "CLIENT_ACTIVATE".equals(type);
    }

    private UUID resolveClientId(Map<String, Object> payload) {
        Object clientId = payload.get("clientId");
        if (clientId == null) {
            clientId = payload.get("clienteId");
        }
        return UUID.fromString(String.valueOf(clientId));
    }
}