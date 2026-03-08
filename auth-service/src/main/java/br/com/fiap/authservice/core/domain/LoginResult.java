package br.com.fiap.authservice.core.domain;

import java.util.List;
import java.util.UUID;

public class LoginResult {
    private final UUID userId;
    private final String accessToken;
    private final long expiresIn;
    private final long refreshExpiresIn;
    private final String tokenType;
    private final List<String> roles;

    public LoginResult(UUID userId, String accessToken, long expiresIn, long refreshExpiresIn,
                       String tokenType, List<String> roles) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.refreshExpiresIn = refreshExpiresIn;
        this.tokenType = tokenType;
        this.roles = roles;
    }

    public UUID getUserId()             { return userId; }
    public String getAccessToken()      { return accessToken; }
    public long getExpiresIn()          { return expiresIn; }
    public long getRefreshExpiresIn()   { return refreshExpiresIn; }
    public String getTokenType()        { return tokenType; }
    public List<String> getRoles()      { return roles; }
}
