package br.com.fiap.client.infra.config;

import br.com.fiap.client.core.gateway.ClientesRepositoryPort;
import br.com.fiap.client.core.usecase.processar_requisicao_orquestrador.ProcessarRequisicaoOrquestradorUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public ProcessarRequisicaoOrquestradorUseCase processarRequisicaoOrquestradorUseCase(
            ClientesRepositoryPort repo
    ) {
        return new ProcessarRequisicaoOrquestradorUseCase(repo);
    }
}