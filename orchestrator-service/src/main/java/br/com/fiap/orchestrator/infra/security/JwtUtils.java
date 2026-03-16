package br.com.fiap.orchestrator.infra.security;

import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

public final class JwtUtils {

    private JwtUtils() {
    }

    public static UUID extractClientId(Jwt jwt) {
        Object claim = jwt.getClaims().get("sub");

        if (claim == null) {
            throw new IllegalArgumentException("Token sem claim 'sub'");
        }

        return UUID.fromString(claim.toString());
    }

    public static String asBearerToken(Jwt jwt) {
        return "Bearer " + jwt.getTokenValue();
    }
}