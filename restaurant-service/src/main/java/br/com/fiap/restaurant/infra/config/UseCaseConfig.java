package br.com.fiap.restaurant.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.fiap.restaurant.core.gateway.RestauranteRepositoryPort;
import br.com.fiap.restaurant.core.usecase.processar_requisicao_orquestrador.ProcessarRequisicaoOrquestradorUseCase;

@Configuration
public class UseCaseConfig {

    @Bean
    public ProcessarRequisicaoOrquestradorUseCase processarRequisicaoOrquestradorUseCase(
            RestauranteRepositoryPort repo
    ) {
        return new ProcessarRequisicaoOrquestradorUseCase(repo);
    }
}