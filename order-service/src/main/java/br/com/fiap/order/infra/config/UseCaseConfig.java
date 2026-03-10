package br.com.fiap.order.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.fiap.order.core.gateway.CatalogClientPort;
import br.com.fiap.order.core.gateway.EventPublisherPort;
import br.com.fiap.order.core.gateway.OrderRepositoryPort;
import br.com.fiap.order.core.gateway.RestaurantClientPort;
import br.com.fiap.order.core.usecase.CreateOrderUseCase;
import br.com.fiap.order.core.usecase.GetOrderUseCase;
import br.com.fiap.order.core.usecase.ListOrdersUseCase;
import br.com.fiap.order.core.usecase.UpdateOrderPaymentStatusUseCase;
@Configuration
public class UseCaseConfig {

    @Bean
    public CreateOrderUseCase createOrderUseCase(OrderRepositoryPort repo,
                                                  CatalogClientPort catalog,
                                                  RestaurantClientPort restaurant,
                                                  EventPublisherPort events) {
        return new CreateOrderUseCase(repo, catalog, restaurant, events);
    }

    @Bean
    public GetOrderUseCase getOrderUseCase(OrderRepositoryPort repo) {
        return new GetOrderUseCase(repo);
    }

    @Bean
    public ListOrdersUseCase listOrdersUseCase(OrderRepositoryPort repo) {
        return new ListOrdersUseCase(repo);
    }

    @Bean
    public UpdateOrderPaymentStatusUseCase updateOrderPaymentStatusUseCase(OrderRepositoryPort repo) {
        return new UpdateOrderPaymentStatusUseCase(repo);
    }
}
