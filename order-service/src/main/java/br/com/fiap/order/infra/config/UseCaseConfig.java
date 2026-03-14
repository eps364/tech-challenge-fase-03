package br.com.fiap.order.infra.config;

import br.com.fiap.order.core.gateway.OrderRepositoryPort;
import br.com.fiap.order.core.usecase.CreateOrderUseCase;
import br.com.fiap.order.core.usecase.FindOrderByIdUseCase;
import br.com.fiap.order.core.usecase.FindOrdersByClientIdUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public CreateOrderUseCase createOrderUseCase(OrderRepositoryPort orderRepositoryPort) {
        return new CreateOrderUseCase(orderRepositoryPort);
    }

    @Bean
    public FindOrderByIdUseCase findOrderByIdUseCase(OrderRepositoryPort orderRepositoryPort) {
        return new FindOrderByIdUseCase(orderRepositoryPort);
    }

    @Bean
    public FindOrdersByClientIdUseCase findOrdersByClientIdUseCase(OrderRepositoryPort orderRepositoryPort) {
        return new FindOrdersByClientIdUseCase(orderRepositoryPort);
    }
}
