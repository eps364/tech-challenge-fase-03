package br.com.fiap.client.infra.messaging.mapper;


import java.time.Instant;

import br.com.fiap.client.core.dto.QueueMessage;
import br.com.fiap.client.core.usecase.ProcessOrchestratorRequestCommand;import br.com.fiap.client.core.usecase.ProcessOrchestratorRequestResult;
public final class MessageMapper {
    private MessageMapper() {
    }

    public static ProcessOrchestratorRequestCommand toCommand(QueueMessage msg) {
        return new ProcessOrchestratorRequestCommand(msg.correlationId(), msg.type(), msg.payload());
    }

    public static QueueMessage toResponseMessage(ProcessOrchestratorRequestResult result) {
        return new QueueMessage(
                result.correlationId(),
                result.responseType(),
                Instant.now(),
                result.responsePayload()
        );
    }
}