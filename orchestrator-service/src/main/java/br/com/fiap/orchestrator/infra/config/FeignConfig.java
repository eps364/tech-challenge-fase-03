package br.com.fiap.orchestrator.infra.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(KeycloakClientCredentialsProperties.class)
public class FeignConfig {
}
