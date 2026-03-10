package br.com.fiap.client.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.fiap.client.core.gateway.ClientesRepositoryPort;
import br.com.fiap.client.core.usecase.ProcessOrchestratorRequestUseCase;
@Configuration
public class UseCaseConfig {

    @Bean
    public ProcessOrchestratorRequestUseCase processOrchestratorRequestUseCase(
            ClientesRepositoryPort repo
    ) {
        return new ProcessOrchestratorRequestUseCase(repo);
    }
}