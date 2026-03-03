package br.com.fiap.orchestrator.core.gateway;

import br.com.fiap.orchestrator.core.dto.MensagemFila;

public interface QueuePublisherPort {
    void publish(String routingKey, MensagemFila mensagem);
}