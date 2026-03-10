package br.com.fiap.payment.infra.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.fiap.payment.core.gateway.EventPublisherPort;
import br.com.fiap.payment.core.gateway.ExternalPaymentGatewayPort;
import br.com.fiap.payment.core.gateway.PaymentRepositoryPort;
import br.com.fiap.payment.core.usecase.ProcessPaymentUseCase;
@Configuration
public class UseCaseConfig {

    @Bean
    public ProcessPaymentUseCase processPaymentUseCase(PaymentRepositoryPort repository,
                                                       ExternalPaymentGatewayPort externalPaymentGateway,
                                                       EventPublisherPort eventPublisher,
                                                       @Value("${app.payment.max-attempts:3}") int maxAttempts) {
        return new ProcessPaymentUseCase(repository, externalPaymentGateway, eventPublisher, maxAttempts);
    }
}
