package br.com.fiap.authservice.infra.web.controller;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.authservice.core.domain.LoginResult;
import br.com.fiap.authservice.core.domain.User;
import br.com.fiap.authservice.core.dto.AuthResponse;
import br.com.fiap.authservice.core.dto.LoginRequest;
import br.com.fiap.authservice.core.dto.RefreshTokenRequest;
import br.com.fiap.authservice.core.dto.RegisterRequest;
import br.com.fiap.authservice.core.usecase.LoginUseCase;
import br.com.fiap.authservice.core.usecase.LogoutUseCase;
import br.com.fiap.authservice.core.usecase.RefreshTokenUseCase;
import br.com.fiap.authservice.core.usecase.RegisterUserUseCase;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUseCase loginUseCase;
    private final LogoutUseCase logoutUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;

    public AuthController(RegisterUserUseCase registerUserUseCase,
                          LoginUseCase loginUseCase,
                          LogoutUseCase logoutUseCase,
                          RefreshTokenUseCase refreshTokenUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.loginUseCase = loginUseCase;
        this.logoutUseCase = logoutUseCase;
        this.refreshTokenUseCase = refreshTokenUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest request) {
        logger.info("Received register request for username {}", request.username());
        try {
            User user = User.create(
                    request.username(),
                    request.email(),
                    request.firstName(),
                    request.lastName()
            );
            User registered = registerUserUseCase.execute(user, request.password());
            logger.info("User registered successfully: {}", registered.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(registered);
        } catch (Exception e) {
            logger.error("Error registering user {}: {}", request.username(), e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        logger.info("Received login request for username {}", request.username());
        try {
            LoginResult result = loginUseCase.execute(request.username(), request.password());
            logger.info("User logged in successfully: {}", result.getUserId());
            return ResponseEntity.ok(toAuthResponse(result));
        } catch (Exception e) {
            logger.error("Error logging in user {}: {}", request.username(), e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshTokenRequest request) {
        logger.info("Received refresh token request");
        try {
            LoginResult result = refreshTokenUseCase.execute(request.refreshToken());
            logger.info("Token refreshed successfully for user {}", result.getUserId());
            return ResponseEntity.ok(toAuthResponse(result));
        } catch (Exception e) {
            logger.error("Error refreshing token: {}", e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            String userId = jwt.getSubject();
            String jti = jwt.getId();
            Instant expiresAt = jwt.getExpiresAt();
            long ttlSeconds = (expiresAt != null)
                    ? Math.max(0, expiresAt.getEpochSecond() - Instant.now().getEpochSecond())
                    : 0L;
            logger.info("Received logout request for user {}", userId);
            try {
                logoutUseCase.execute(userId, jti, ttlSeconds);
                logger.info("User logged out successfully: {}", userId);
                return ResponseEntity.noContent().build();
            } catch (Exception e) {
                logger.error("Error logging out user {}: {}", userId, e.getMessage(), e);
                throw e;
            }
        }
        logger.warn("Unauthorized logout attempt");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    private AuthResponse toAuthResponse(LoginResult result) {
        return new AuthResponse(
                result.getUserId(),
                result.getAccessToken(),
                result.getRefreshToken(),
                result.getExpiresIn(),
                result.getRefreshExpiresIn(),
                result.getTokenType(),
                result.getRoles()
        );
    }
}