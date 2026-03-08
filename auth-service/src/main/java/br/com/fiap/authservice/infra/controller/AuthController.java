package br.com.fiap.authservice.infra.controller;

import java.time.Instant;

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
import br.com.fiap.authservice.core.usecase.LoginUseCase;
import br.com.fiap.authservice.core.usecase.LogoutUseCase;
import br.com.fiap.authservice.core.usecase.RegisterUserUseCase;
import br.com.fiap.authservice.infra.controller.dto.AuthResponse;
import br.com.fiap.authservice.infra.controller.dto.LoginRequest;
import br.com.fiap.authservice.infra.controller.dto.RegisterRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUseCase loginUseCase;
    private final LogoutUseCase logoutUseCase;

    public AuthController(RegisterUserUseCase registerUserUseCase,
                          LoginUseCase loginUseCase,
                          LogoutUseCase logoutUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.loginUseCase = loginUseCase;
        this.logoutUseCase = logoutUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest request) {
        User user = new User(
                null,
                request.getUsername(),
                request.getEmail(),
                request.getFirstName(),
                request.getLastName()
        );
        User registered = registerUserUseCase.execute(user, request.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(registered);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        LoginResult result = loginUseCase.execute(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(new AuthResponse(
                result.getUserId(),
                result.getAccessToken(),
                result.getExpiresIn(),
                result.getRefreshExpiresIn(),
                result.getTokenType(),
                result.getRoles()
        ));
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
            logoutUseCase.execute(userId, jti, ttlSeconds);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
