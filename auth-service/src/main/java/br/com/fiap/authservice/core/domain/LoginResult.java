package br.com.fiap.authservice.core.domain;

import java.util.List;
import java.util.UUID;

public class LoginResult {
    private final UUID userId;
    private final String accessToken;
    private final String refreshToken;
    private final long expiresIn;
    private final long refreshExpiresIn;
    private final String tokenType;
    private final List<String> roles;

    public LoginResult(UUID userId, String accessToken, String refreshToken,
                       long expiresIn, long refreshExpiresIn,
                       String tokenType, List<String> roles) {
        validate(userId, accessToken, refreshToken, expiresIn, refreshExpiresIn, tokenType, roles);
        this.userId = userId;
        this.accessToken = accessToken.trim();
        this.refreshToken = refreshToken.trim();
        this.expiresIn = expiresIn;
        this.refreshExpiresIn = refreshExpiresIn;
        this.tokenType = tokenType.trim();
        this.roles = roles != null ? List.copyOf(roles) : List.of();
    }

    private void validate(UUID userId, String accessToken, String refreshToken,
                          long expiresIn, long refreshExpiresIn,
                          String tokenType, List<String> roles) {
        if (userId == null) {
            throw new ValidationException("userId", "The user id is required");
        }
        if (accessToken == null || accessToken.isBlank()) {
            throw new ValidationException("accessToken", "The access token is required");
        }
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new ValidationException("refreshToken", "The refresh token is required");
        }
        if (expiresIn <= 0) {
            throw new ValidationException("expiresIn", "The expiresIn must be greater than zero");
        }
        if (refreshExpiresIn <= 0) {
            throw new ValidationException("refreshExpiresIn", "The refreshExpiresIn must be greater than zero");
        }
        if (tokenType == null || tokenType.isBlank()) {
            throw new ValidationException("tokenType", "The token type is required");
        }
        if (roles != null && roles.stream().anyMatch(role -> role == null || role.isBlank())) {
            throw new ValidationException("roles", "The roles cannot contain blank values");
        }
    }

    public UUID getUserId()             { return userId; }
    public String getAccessToken()      { return accessToken; }
    public String getRefreshToken()     { return refreshToken; }
    public long getExpiresIn()          { return expiresIn; }
    public long getRefreshExpiresIn()   { return refreshExpiresIn; }
    public String getTokenType()        { return tokenType; }
    public List<String> getRoles()      { return roles; }
}

