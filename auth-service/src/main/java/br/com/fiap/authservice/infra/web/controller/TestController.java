package br.com.fiap.authservice.infra.web.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test/public")
    public Map<String, String> publicEndpoint() {
        return Map.of("message", "Public endpoint for auth-service");
    }

    @GetMapping("/test/private")
    public Map<String, Object> privateEndpoint(@AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        List<String> roles = realmAccess != null && realmAccess.get("roles") instanceof List<?> rolesValue
            ? rolesValue.stream().map(String::valueOf).toList()
            : Collections.emptyList();

        return Map.of(
            "message", "Private endpoint for auth-service",
            "user", jwt.getSubject(),
            "roles", roles
        );
    }
}