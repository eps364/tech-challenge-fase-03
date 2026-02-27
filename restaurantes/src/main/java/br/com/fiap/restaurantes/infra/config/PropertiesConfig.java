package br.com.fiap.restaurantes.infra.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RabbitQueuesProperties.class)
public class PropertiesConfig {}