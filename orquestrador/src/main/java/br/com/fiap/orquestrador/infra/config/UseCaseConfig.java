package br.com.fiap.orquestrador.infra.config;

import br.com.fiap.orquestrador.core.gateway.QueuePublisherPort;
import br.com.fiap.orquestrador.core.usecase.enviar_para_clientes.EnviarParaClientesUseCase;
import br.com.fiap.orquestrador.core.usecase.enviar_para_pedidos.EnviarParaPedidosUseCase;
import br.com.fiap.orquestrador.core.usecase.enviar_para_restaurantes.EnviarParaRestaurantesUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    EnviarParaClientesUseCase enviarParaClientesUseCase(QueuePublisherPort publisher) {
        return new EnviarParaClientesUseCase(publisher);
    }

    @Bean
    EnviarParaRestaurantesUseCase enviarParaRestaurantesUseCase(QueuePublisherPort publisher) {
        return new EnviarParaRestaurantesUseCase(publisher);
    }

    @Bean
    EnviarParaPedidosUseCase enviarParaPedidosUseCase(QueuePublisherPort publisher) {
        return new EnviarParaPedidosUseCase(publisher);
    }
}