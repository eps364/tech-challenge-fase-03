package br.com.fiap.restaurantes.core.usecase.processar_requisicao_orquestrador;

import java.util.Map;

public record ProcessarRequisicaoCommand(
        String correlationId,
        String tipo,
        Map<String, Object> payload
) {}