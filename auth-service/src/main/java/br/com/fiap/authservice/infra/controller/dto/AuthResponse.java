package br.com.fiap.authservice.infra.controller.dto;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthResponse {

    @JsonProperty("userId")
    private UUID userId;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("expires_in")
    private long expiresIn;

    @JsonProperty("refresh_expires_in")
    private long refreshExpiresIn;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("roles")
    private List<String> roles;

    public AuthResponse() {}

    public AuthResponse(UUID userId, String accessToken, String refreshToken,
                        long expiresIn, long refreshExpiresIn,
                        String tokenType, List<String> roles) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.refreshExpiresIn = refreshExpiresIn;
        this.tokenType = tokenType;
        this.roles = roles;
    }

    public UUID getUserId()                         { return userId; }
    public void setUserId(UUID userId)               { this.userId = userId; }
    public String getAccessToken()                  { return accessToken; }
    public void setAccessToken(String accessToken)  { this.accessToken = accessToken; }
    public String getRefreshToken()                 { return refreshToken; }
    public void setRefreshToken(String refreshToken){ this.refreshToken = refreshToken; }
    public long getExpiresIn()                      { return expiresIn; }
    public void setExpiresIn(long expiresIn)        { this.expiresIn = expiresIn; }
    public long getRefreshExpiresIn()               { return refreshExpiresIn; }
    public void setRefreshExpiresIn(long v)         { this.refreshExpiresIn = v; }
    public String getTokenType()                    { return tokenType; }
    public void setTokenType(String tokenType)      { this.tokenType = tokenType; }
    public List<String> getRoles()                  { return roles; }
    public void setRoles(List<String> roles)        { this.roles = roles; }
}

