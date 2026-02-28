package br.com.fiap.client.infra.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RabbitQueuesProperties.class)
public class PropertiesConfig {}