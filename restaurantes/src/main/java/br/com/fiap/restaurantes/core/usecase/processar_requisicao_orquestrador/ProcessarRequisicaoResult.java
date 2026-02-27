package br.com.fiap.restaurantes.core.usecase.processar_requisicao_orquestrador;

import java.util.Map;

public record ProcessarRequisicaoResult(
        String correlationId,
        String tipoResposta,
        Map<String, Object> payloadResposta
) {}