package br.com.fiap.client.core.gateway;


import br.com.fiap.client.core.dto.MensagemFila;

public interface QueuePublisherPort {
    void publish(String routingKey, MensagemFila mensagem);
}