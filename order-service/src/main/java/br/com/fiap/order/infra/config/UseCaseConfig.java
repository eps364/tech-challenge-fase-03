package br.com.fiap.order.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.fiap.order.core.gateway.EventPublisherPort;
import br.com.fiap.order.core.gateway.OrderPreparationEventPublisherPort;
import br.com.fiap.order.core.gateway.OrderRepositoryPort;
import br.com.fiap.order.core.usecase.CreateOrderUseCase;
import br.com.fiap.order.core.usecase.FindOrderByIdUseCase;
import br.com.fiap.order.core.usecase.FindOrdersByClientIdUseCase;
import br.com.fiap.order.core.usecase.UpdateOrderPaymentStatusUseCase;

@Configuration
public class UseCaseConfig {

    @Bean
    public CreateOrderUseCase createOrderUseCase(OrderRepositoryPort orderRepositoryPort,
                                                 EventPublisherPort eventPublisherPort) {
        return new CreateOrderUseCase(orderRepositoryPort, eventPublisherPort);
    }

    @Bean
    public FindOrderByIdUseCase findOrderByIdUseCase(OrderRepositoryPort orderRepositoryPort) {
        return new FindOrderByIdUseCase(orderRepositoryPort);
    }

    @Bean
    public FindOrdersByClientIdUseCase findOrdersByClientIdUseCase(OrderRepositoryPort orderRepositoryPort) {
        return new FindOrdersByClientIdUseCase(orderRepositoryPort);
    }

    @Bean
    public UpdateOrderPaymentStatusUseCase updateOrderPaymentStatusUseCase(
            OrderRepositoryPort orderRepositoryPort,
            OrderPreparationEventPublisherPort orderPreparationEventPublisherPort
    ) {
        return new UpdateOrderPaymentStatusUseCase(
                orderRepositoryPort,
                orderPreparationEventPublisherPort
        );
    }
}