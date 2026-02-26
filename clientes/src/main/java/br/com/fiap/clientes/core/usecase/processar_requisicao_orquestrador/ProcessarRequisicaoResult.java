package br.com.fiap.clientes.core.usecase.processar_requisicao_orquestrador;

import java.util.Map;

public record ProcessarRequisicaoResult(
        String correlationId,
        String tipoResposta,
        Map<String, Object> payloadResposta
) {}