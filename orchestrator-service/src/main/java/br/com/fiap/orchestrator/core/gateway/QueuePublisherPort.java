package br.com.fiap.orchestrator.core.gateway;

import br.com.fiap.orchestrator.core.dto.RequestEvent;

public interface QueuePublisherPort {
    void publish(String routingKey, RequestEvent mensagem);
}