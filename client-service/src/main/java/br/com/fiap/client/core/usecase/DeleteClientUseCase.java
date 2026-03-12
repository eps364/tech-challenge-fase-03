package br.com.fiap.client.core.usecase;

import java.util.UUID;

import br.com.fiap.client.core.gateway.ClientRepositoryPort;

public class DeleteClientUseCase {

    private final ClientRepositoryPort repo;

    public DeleteClientUseCase(ClientRepositoryPort repo) {
        this.repo = repo;
    }

    public void execute(UUID targetId, UUID callerId, boolean isAdmin) {
        validateOwnership(targetId, callerId, isAdmin);

        if (!repo.existsById(targetId)) {
            throw new ClientNotFoundException("Client not found: " + targetId);
        }

        repo.deleteById(targetId);
    }

    private void validateOwnership(UUID targetId, UUID callerId, boolean isAdmin) {
        if (!isAdmin && !targetId.equals(callerId)) {
            throw new ClientAccessDeniedException("User cannot delete another user profile");
        }
    }
}
