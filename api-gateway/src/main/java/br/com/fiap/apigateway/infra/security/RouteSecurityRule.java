package br.com.fiap.apigateway.infra.security;

import java.util.List;

import org.springframework.http.HttpMethod;

public record RouteSecurityRule(
        Integer id,
        HttpMethod method,
        String pathPattern,
        RouteAccessType access,
        List<String> roles
) {
}
