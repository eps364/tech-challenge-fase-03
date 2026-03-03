package br.com.fiap.client.infra.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.rabbit")
public record RabbitQueuesProperties(
        String exchange,
        Queues queues
) {
    public record Queues(In in, Out out) {
        public record In(String orquestradorClientes) {}
        public record Out(String clientesOrquestrador) {}
    }
}