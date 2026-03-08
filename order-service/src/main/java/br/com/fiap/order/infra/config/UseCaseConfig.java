package br.com.fiap.order.infra.config;

import br.com.fiap.order.core.gateway.CatalogClientPort;
import br.com.fiap.order.core.gateway.EventPublisherPort;
import br.com.fiap.order.core.gateway.OrderRepositoryPort;
import br.com.fiap.order.core.usecase.confirmorder.ConfirmOrderUseCase;
import br.com.fiap.order.core.usecase.createorder.CreateOrderUseCase;
import br.com.fiap.order.core.usecase.getorder.GetOrderUseCase;
import br.com.fiap.order.core.usecase.listorders.ListOrdersUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public CreateOrderUseCase createOrderUseCase(OrderRepositoryPort repo,
                                                  CatalogClientPort catalog) {
        return new CreateOrderUseCase(repo, catalog);
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
}
