package com.example.consumer;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.amqp.autoconfigure.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConsumerConfig {

    @Value("${app.queues.main}")
    private String mainQueue;

    @Value("${app.queues.dlq}")
    private String dlqQueue;

    @Value("${app.queues.error}")
    private String errorQueue;

    @Value("${app.retry.delay-ms}")
    private int retryDelayMs;

    // Exchanges (Direct)
    @Bean
    public DirectExchange mainExchange() {
        return new DirectExchange("ex.main", true, false);
    }

    @Bean
    public DirectExchange retryExchange() {
        return new DirectExchange("ex.retry", true, false);
    }

    @Bean
    public DirectExchange errorExchange() {
        return new DirectExchange("ex.error", true, false);
    }

    // MAIN queue: se rejeitar, manda para retry exchange (DLQ)
    @Bean
    public Queue mainQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", "ex.retry");
        args.put("x-dead-letter-routing-key", dlqQueue);
        return new Queue(mainQueue, true, false, false, args);
    }

    // DLQ (retry): segura por TTL e manda de volta pra main
    @Bean
    public Queue dlqQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-message-ttl", retryDelayMs);
        args.put("x-dead-letter-exchange", "ex.main");
        args.put("x-dead-letter-routing-key", mainQueue);
        return new Queue(dlqQueue, true, false, false, args);
    }

    // ERROR queue: parking lot
    @Bean
    public Queue errorQueue() {
        return new Queue(errorQueue, true);
    }

    // Bindings
    @Bean
    public Binding bindMain(Queue mainQueue, DirectExchange mainExchange) {
        return BindingBuilder.bind(mainQueue).to(mainExchange).with(mainQueue.getName());
    }

    @Bean
    public Binding bindDlq(Queue dlqQueue, DirectExchange retryExchange) {
        return BindingBuilder.bind(dlqQueue).to(retryExchange).with(dlqQueue.getName());
    }

    @Bean
    public Binding bindError(Queue errorQueue, DirectExchange errorExchange) {
        return BindingBuilder.bind(errorQueue).to(errorExchange).with(errorQueue.getName());
    }

    /**
     * Listener com ACK manual (pra decidir: ack, reject pra DLQ, ou mandar pro error).
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            SimpleRabbitListenerContainerFactoryConfigurer configurer,
            ConnectionFactory connectionFactory
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        // importante: não re-enfileirar automaticamente em erro
        factory.setDefaultRequeueRejected(false);
        return factory;
    }
}
