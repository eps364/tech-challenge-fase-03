package br.com.fiap.restaurant.core.usecase.processar_requisicao_orquestrador;


import java.util.Map;
import java.util.UUID;

import br.com.fiap.restaurant.core.domain.Restaurante;
import br.com.fiap.restaurant.core.gateway.RestauranteRepositoryPort;

public class ProcessarRequisicaoOrquestradorUseCase {

    private final RestauranteRepositoryPort repo;

    public ProcessarRequisicaoOrquestradorUseCase(RestauranteRepositoryPort repo) {
        this.repo = repo;
    }

    public ProcessarRequisicaoResult execute(ProcessarRequisicaoCommand cmd) {

        if ("RESTAURANTE_ATIVAR".equals(cmd.tipo())) {
            UUID restauranteId = UUID.fromString(String.valueOf(cmd.payload().get("restauranteId")));

            Restaurante r = repo.findById(restauranteId)
                    .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + restauranteId));

            Restaurante atualizado = r.ativar();
            repo.save(atualizado);

            return new ProcessarRequisicaoResult(
                    cmd.correlationId(),
                    "RESTAURANTE_ATIVADO",
                    Map.of("restauranteId", atualizado.getId().toString(), "ativo", atualizado.isAtivo())
            );
        }

        return new ProcessarRequisicaoResult(
                cmd.correlationId(),
                "RESTAURANTES_ACK",
                Map.of("mensagem", "Tipo não tratado: " + cmd.tipo())
        );
    }
}