package br.com.fiap.client.infra.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.rabbit")
public record RabbitQueuesProperties(
        String exchange,
        Queues queues
) {
    public record Queues(In in, Out out) {
        public record In(String orchestratorClients) {}
        public record Out(String clientsOrchestrator) {}
    }
}