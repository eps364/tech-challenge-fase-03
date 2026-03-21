package br.com.fiap.payment.infra.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.fiap.payment.core.gateway.EventPublisherPort;
import br.com.fiap.payment.core.gateway.PaymentRepositoryPort;
import br.com.fiap.payment.core.gateway.ProcPagGatewayPort;
import br.com.fiap.payment.core.usecase.GetPaymentByOrderIdUseCase;
import br.com.fiap.payment.core.usecase.PollPendingPaymentsUseCase;
import br.com.fiap.payment.core.usecase.StartPaymentProcessUseCase;

@Configuration
public class UseCaseConfig {

    @Bean
    public StartPaymentProcessUseCase startPaymentProcessUseCase(PaymentRepositoryPort repository,
                                                                 ProcPagGatewayPort procPagGatewayPort,
                                                                 EventPublisherPort eventPublisherPort) {
        return new StartPaymentProcessUseCase(repository, procPagGatewayPort, eventPublisherPort);
    }

    @Bean
    public PollPendingPaymentsUseCase pollPendingPaymentsUseCase(PaymentRepositoryPort repository,
                                                                 ProcPagGatewayPort procPagGatewayPort,
                                                                 EventPublisherPort eventPublisherPort,
                                                                 @Value("${app.payment.max-attempts:3}") int maxAttempts) {
        return new PollPendingPaymentsUseCase(repository, procPagGatewayPort, eventPublisherPort, maxAttempts);
    }

    @Bean
    public GetPaymentByOrderIdUseCase getPaymentByOrderIdUseCase(PaymentRepositoryPort repository) {
        return new GetPaymentByOrderIdUseCase(repository);
    }
}