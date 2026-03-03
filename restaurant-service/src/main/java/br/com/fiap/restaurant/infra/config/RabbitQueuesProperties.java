package br.com.fiap.restaurant.infra.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.rabbit")
public record RabbitQueuesProperties(
        String exchange,
        Queues queues
) {
    public record Queues(In in, Out out) {
        public record In(String orquestradorRestaurantes) {}
        public record Out(String restaurantesOrquestrador) {}
    }
}