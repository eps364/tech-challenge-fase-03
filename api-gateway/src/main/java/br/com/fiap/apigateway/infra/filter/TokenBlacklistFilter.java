package br.com.fiap.apigateway.infra.filter;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class TokenBlacklistFilter implements GlobalFilter, Ordered {

    private static final String BLACKLIST_PREFIX = "blacklist:";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final ReactiveStringRedisTemplate redisTemplate;

    public TokenBlacklistFilter(ReactiveStringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public int getOrder() {
        // Run before Spring Security authentication filter (order -1)
        return Ordered.HIGHEST_PRECEDENCE + 10;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            return chain.filter(exchange);
        }

        String token = authHeader.substring(BEARER_PREFIX.length()).trim();
        String jti = extractJti(token);
        if (jti == null) {
            return chain.filter(exchange);
        }

        return redisTemplate.hasKey(BLACKLIST_PREFIX + jti)
                .flatMap(blacklisted -> {
                    if (Boolean.TRUE.equals(blacklisted)) {
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    }
                    return chain.filter(exchange);
                });
    }

    private String extractJti(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length < 2) {
                return null;
            }
            byte[] payloadBytes = Base64.getUrlDecoder().decode(padBase64(parts[1]));
            String payloadJson = new String(payloadBytes, StandardCharsets.UTF_8);
            JsonNode node = OBJECT_MAPPER.readTree(payloadJson);
            JsonNode jtiNode = node.get("jti");
            return (jtiNode != null && !jtiNode.isNull()) ? jtiNode.asText() : null;
        } catch (IllegalArgumentException | com.fasterxml.jackson.core.JacksonException e) {
            return null;
        }
    }

    private String padBase64(String base64) {
        int padding = base64.length() % 4;
        if (padding == 2) return base64 + "==";
        if (padding == 3) return base64 + "=";
        return base64;
    }
}
