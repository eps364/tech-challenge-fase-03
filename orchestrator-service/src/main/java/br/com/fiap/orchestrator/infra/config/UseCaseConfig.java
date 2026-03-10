package br.com.fiap.orchestrator.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.fiap.orchestrator.core.gateway.QueuePublisherPort;
import br.com.fiap.orchestrator.core.usecase.SendToClientsUseCase;import br.com.fiap.orchestrator.core.usecase.SendToOrdersUseCase;import br.com.fiap.orchestrator.core.usecase.SendToPaymentsUseCase;import br.com.fiap.orchestrator.core.usecase.SendToPaymentsWorkerUseCase;import br.com.fiap.orchestrator.core.usecase.SendToRestaurantsUseCase;
@Configuration
public class UseCaseConfig {

    @Bean
    SendToClientsUseCase sendToClientsUseCase(QueuePublisherPort publisher) {
        return new SendToClientsUseCase(publisher);
    }

    @Bean
    SendToRestaurantsUseCase sendToRestaurantsUseCase(QueuePublisherPort publisher) {
        return new SendToRestaurantsUseCase(publisher);
    }

    @Bean
    SendToOrdersUseCase sendToOrdersUseCase(QueuePublisherPort publisher) {
        return new SendToOrdersUseCase(publisher);
    }

    @Bean
    SendToPaymentsUseCase sendToPaymentsUseCase(QueuePublisherPort publisher) {
        return new SendToPaymentsUseCase(publisher);
    }

    @Bean
    SendToPaymentsWorkerUseCase sendToPaymentsWorkerUseCase(QueuePublisherPort publisher) {
        return new SendToPaymentsWorkerUseCase(publisher);
    }
}