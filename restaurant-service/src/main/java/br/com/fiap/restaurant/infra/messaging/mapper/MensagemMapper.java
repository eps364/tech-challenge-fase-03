package br.com.fiap.restaurant.infra.messaging.mapper;


import br.com.fiap.restaurant.core.dto.MensagemFila;
import br.com.fiap.restaurant.core.usecase.processar_requisicao_orquestrador.ProcessarRequisicaoCommand;
import br.com.fiap.restaurant.core.usecase.processar_requisicao_orquestrador.ProcessarRequisicaoResult;

import java.time.Instant;

public final class MensagemMapper {
    private MensagemMapper() {
    }

    public static ProcessarRequisicaoCommand toCommand(MensagemFila msg) {
        return new ProcessarRequisicaoCommand(msg.correlationId(), msg.tipo(), msg.payload());
    }

    public static MensagemFila toMensagemRetorno(ProcessarRequisicaoResult result) {
        return new MensagemFila(
                result.correlationId(),
                result.tipoResposta(),
                Instant.now(),
                result.payloadResposta()
        );
    }
}