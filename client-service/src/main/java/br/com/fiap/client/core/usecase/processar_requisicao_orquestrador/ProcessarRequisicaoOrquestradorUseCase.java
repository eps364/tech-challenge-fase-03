package br.com.fiap.client.core.usecase.processar_requisicao_orquestrador;


import br.com.fiap.client.core.domain.Cliente;
import br.com.fiap.client.core.gateway.ClientesRepositoryPort;

import java.util.Map;
import java.util.UUID;

public class ProcessarRequisicaoOrquestradorUseCase {

    private final ClientesRepositoryPort repo;

    public ProcessarRequisicaoOrquestradorUseCase(ClientesRepositoryPort repo) {
        this.repo = repo;
    }

    public ProcessarRequisicaoResult execute(ProcessarRequisicaoCommand cmd) {

        if ("CLIENTE_ATIVAR".equals(cmd.tipo())) {
            UUID clienteId = UUID.fromString(String.valueOf(cmd.payload().get("clienteId")));

            Cliente cliente = repo.findById(clienteId)
                    .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + clienteId));

            Cliente atualizado = cliente.ativar();
            repo.save(atualizado);

            return new ProcessarRequisicaoResult(
                    cmd.correlationId(),
                    "CLIENTE_ATIVADO",
                    Map.of(
                            "clienteId", atualizado.getId().toString(),
                            "ativo", atualizado.isAtivo()
                    )
            );
        }

        return new ProcessarRequisicaoResult(
                cmd.correlationId(),
                "CLIENTES_ACK",
                Map.of("mensagem", "Tipo não tratado: " + cmd.tipo())
        );
    }
}