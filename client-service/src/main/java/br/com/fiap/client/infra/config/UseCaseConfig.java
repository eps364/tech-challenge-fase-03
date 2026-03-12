package br.com.fiap.client.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.fiap.client.core.gateway.ClientRepositoryPort;
import br.com.fiap.client.core.usecase.CreateClientUseCase;
import br.com.fiap.client.core.usecase.DeleteClientUseCase;
import br.com.fiap.client.core.usecase.GetClientUseCase;
import br.com.fiap.client.core.usecase.ProcessOrchestratorRequestUseCase;
import br.com.fiap.client.core.usecase.UpdateClientUseCase;

@Configuration
public class UseCaseConfig {

    @Bean
    public CreateClientUseCase createClientUseCase(ClientRepositoryPort repo) {
        return new CreateClientUseCase(repo);
    }

    @Bean
    public GetClientUseCase getClientUseCase(ClientRepositoryPort repo) {
        return new GetClientUseCase(repo);
    }

    @Bean
    public UpdateClientUseCase updateClientUseCase(ClientRepositoryPort repo) {
        return new UpdateClientUseCase(repo);
    }

    @Bean
    public DeleteClientUseCase deleteClientUseCase(ClientRepositoryPort repo) {
        return new DeleteClientUseCase(repo);
    }

    @Bean
    public ProcessOrchestratorRequestUseCase processOrchestratorRequestUseCase(
            ClientRepositoryPort repo
    ) {
        return new ProcessOrchestratorRequestUseCase(repo);
    }
}