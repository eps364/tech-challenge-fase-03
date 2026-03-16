package br.com.fiap.orchestrator.infra.config;

import br.com.fiap.orchestrator.infra.security.SystemAccessTokenProvider;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignAuthInterceptorConfig {

    @Bean
    public RequestInterceptor bearerTokenRequestInterceptor(SystemAccessTokenProvider systemAccessTokenProvider) {
        return requestTemplate -> {
            System.out.println("interceptor do feign executado");
            String token = systemAccessTokenProvider.getAccessToken();
            System.out.println("token obtido");
            requestTemplate.header("Authorization", "Bearer " + token);
        };
    }
}
