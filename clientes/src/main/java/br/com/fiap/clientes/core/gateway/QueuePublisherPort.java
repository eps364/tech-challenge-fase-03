package br.com.fiap.clientes.core.gateway;

import br.com.fiap.clientes.core.dto.MensagemFila;

public interface QueuePublisherPort {
    void publish(String routingKey, MensagemFila mensagem);
}