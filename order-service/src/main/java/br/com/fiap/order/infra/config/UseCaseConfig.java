package br.com.fiap.order.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.fiap.order.core.gateway.CatalogClientPort;
import br.com.fiap.order.core.gateway.EventPublisherPort;
import br.com.fiap.order.core.gateway.OrderRepositoryPort;
import br.com.fiap.order.core.gateway.RestaurantClientPort;
import br.com.fiap.order.core.usecase.ConfirmOrderUseCase;import br.com.fiap.order.core.usecase.CreateOrderUseCase;import br.com.fiap.order.core.usecase.GetOrderUseCase;import br.com.fiap.order.core.usecase.ListOrdersUseCase;import br.com.fiap.order.core.usecase.UpdateOrderPaymentStatusUseCase;
@Configuration
public class UseCaseConfig {

    @Bean
    public CreateOrderUseCase createOrderUseCase(OrderRepositoryPort repo,
                                                  CatalogClientPort catalog,
                                                  RestaurantClientPort restaurant) {
        return new CreateOrderUseCase(repo, catalog, restaurant);
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
    public ConfirmOrderUseCase confirmOrderUseCase(OrderRepositoryPort repo,
                                                    EventPublisherPort events) {
        return new ConfirmOrderUseCase(repo, events);
    }

    @Bean
    public UpdateOrderPaymentStatusUseCase updateOrderPaymentStatusUseCase(OrderRepositoryPort repo) {
        return new UpdateOrderPaymentStatusUseCase(repo);
    }
}
