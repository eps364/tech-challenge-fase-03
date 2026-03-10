package br.com.fiap.apigateway.core.dto;

import java.util.List;

public record RouteSecurityRuleRequest(
        Integer id,
        String method,
        String pathPattern,
        String access,
        List<String> roles
) {
}
