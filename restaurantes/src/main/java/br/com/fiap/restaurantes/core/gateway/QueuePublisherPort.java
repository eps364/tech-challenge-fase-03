package br.com.fiap.restaurantes.core.gateway;

import br.com.fiap.restaurantes.core.dto.MensagemFila;

public interface QueuePublisherPort {
    void publish(String routingKey, MensagemFila mensagem);
}