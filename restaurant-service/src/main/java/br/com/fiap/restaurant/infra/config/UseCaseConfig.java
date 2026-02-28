package br.com.fiap.restaurant.infra.config;

import br.com.fiap.restaurant.core.gateway.RestauranteRepositoryPort;
import br.com.fiap.restaurant.core.usecase.processar_requisicao_orquestrador.ProcessarRequisicaoOrquestradorUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    ProcessarRequisicaoOrquestradorUseCase processarRequisicaoOrquestradorUseCase(RestauranteRepositoryPort repo) {
        return new ProcessarRequisicaoOrquestradorUseCase(repo);
    }
}