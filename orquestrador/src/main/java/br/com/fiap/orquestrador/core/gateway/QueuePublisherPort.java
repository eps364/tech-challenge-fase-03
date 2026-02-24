package br.com.fiap.orquestrador.core.gateway;

import br.com.fiap.orquestrador.core.dto.MensagemFila;

public interface QueuePublisherPort {
    void publish(String routingKey, MensagemFila mensagem);
}