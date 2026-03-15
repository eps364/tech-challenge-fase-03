package br.com.fiap.orchestrator.infra.config;

import br.com.fiap.orchestrator.core.gateway.CatalogGateway;
import br.com.fiap.orchestrator.core.gateway.ClientGateway;
import br.com.fiap.orchestrator.core.gateway.OrderGateway;
import br.com.fiap.orchestrator.core.usecase.CreateOrderOrchestrationUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public CreateOrderOrchestrationUseCase createOrderOrchestrationUseCase(
            ClientGateway clientGateway,
            CatalogGateway catalogGateway,
            OrderGateway orderGateway
    ) {
        return new CreateOrderOrchestrationUseCase(
                clientGateway,
                catalogGateway,
                orderGateway
        );
    }
}