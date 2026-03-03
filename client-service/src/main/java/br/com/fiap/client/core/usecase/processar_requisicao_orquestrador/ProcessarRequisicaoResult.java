package br.com.fiap.client.core.usecase.processar_requisicao_orquestrador;

import java.util.Map;

public record ProcessarRequisicaoResult(
        String correlationId,
        String tipoResposta,
        Map<String, Object> payloadResposta
) {}