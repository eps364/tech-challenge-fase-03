package br.com.fiap.client.infra.controller;

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
        return Map.of("message", "Public endpoint for client-service");
    }

    @GetMapping("/test/private")
    public Map<String, Object> privateEndpoint(@AuthenticationPrincipal Jwt jwt) {
        List<String> roles = extractRoles(jwt);

        return Map.of(
            "message", "Private endpoint for client-service",
            "user", jwt.getSubject(),
            "roles", roles
        );
    }

    private List<String> extractRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");

        if (realmAccess == null || !(realmAccess.get("roles") instanceof List<?> rolesValue)) {
            return Collections.emptyList();
        }

        return rolesValue.stream().map(String::valueOf).toList();
    }
}
