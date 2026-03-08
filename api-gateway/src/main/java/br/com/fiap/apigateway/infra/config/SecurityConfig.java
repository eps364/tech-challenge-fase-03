package br.com.fiap.apigateway.infra.config;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import reactor.core.publisher.Mono;

@Configuration
public class SecurityConfig {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        return http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(unauthorizedEntryPoint())
            )
            .authorizeExchange(exchange -> exchange
                .pathMatchers(
                    "/auth-service/auth/register",
                    "/auth-service/auth/login",
                    "/auth-service/auth/refresh",
                    "/auth-service/auth/logout",
                    "/auth-service/test/public",
                    "/order-service/test/public",
                    "/payment-service/test/public",
                    "/restaurant-service/test/public",
                    "/client-service/test/public",
                    "/catalog-service/test/public"
                ).permitAll()
                .pathMatchers(HttpMethod.GET,
                    "/catalog-service/products",
                    "/catalog-service/products/**",
                    "/restaurant-service/restaurants",
                    "/restaurant-service/restaurants/**"
                ).permitAll()
                .pathMatchers(HttpMethod.POST,
                    "/catalog-service/products",
                    "/catalog-service/products/**"
                ).hasAnyRole("owner", "admin")
                .pathMatchers(HttpMethod.PUT,
                    "/catalog-service/products",
                    "/catalog-service/products/**"
                ).hasAnyRole("owner", "admin")
                .pathMatchers(HttpMethod.DELETE,
                    "/catalog-service/products",
                    "/catalog-service/products/**"
                ).hasAnyRole("owner", "admin")
                .pathMatchers(
                    "/auth-service/test/private",
                    "/order-service/test/private",
                    "/payment-service/test/private",
                    "/restaurant-service/test/private",
                    "/client-service/test/private",
                    "/catalog-service/test/private"
                ).hasRole("user")
                .anyExchange().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(keycloakJwtConverter()))
                .authenticationEntryPoint(unauthorizedEntryPoint())
            )
            .build();
    }

    @Bean
    public ServerAuthenticationEntryPoint unauthorizedEntryPoint() {
        return (exchange, ex) -> {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

            ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                    HttpStatus.UNAUTHORIZED, "JWT token is invalid or expired");
            problem.setTitle("Unauthorized");
            problem.setProperty("timestamp", Instant.now().toString());
            problem.setInstance(java.net.URI.create(
                    exchange.getRequest().getURI().getPath()));

            return Mono.fromCallable(() -> objectMapper.writeValueAsBytes(problem))
                    .flatMap(bytes -> {
                        response.getHeaders().setContentLength(bytes.length);
                        return response.writeWith(Mono.just(
                                response.bufferFactory().wrap(bytes)));
                    });
        };
    }

    @Bean
    public Converter<Jwt, Mono<AbstractAuthenticationToken>> keycloakJwtConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            if (realmAccess == null || !(realmAccess.get("roles") instanceof List<?> roles)) {
                return Collections.emptyList();
            }
            return roles.stream()
                .map(String::valueOf)
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
        });
        return new ReactiveJwtAuthenticationConverterAdapter(converter);
    }
}
