package br.com.fiap.authservice.infra.web.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.fiap.authservice.core.domain.User;
import br.com.fiap.authservice.core.dto.AuthResponse;
import br.com.fiap.authservice.core.dto.LoginRequest;
import br.com.fiap.authservice.core.dto.RefreshTokenRequest;
import br.com.fiap.authservice.core.dto.RegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Auth", description = "API for authentication and user management")
public interface AuthAPI {

    @Operation(summary = "Register a new user", description = "Registers a new user in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/register")
    ResponseEntity<User> register(@RequestBody RegisterRequest request);

    @Operation(summary = "Login user", description = "Authenticates a user and returns JWT tokens")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User authenticated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid credentials"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/login")
    ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request);

    @Operation(summary = "Refresh token", description = "Refreshes JWT tokens for a user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid refresh token"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/refresh")
    ResponseEntity<AuthResponse> refresh(@RequestBody RefreshTokenRequest request);
}
