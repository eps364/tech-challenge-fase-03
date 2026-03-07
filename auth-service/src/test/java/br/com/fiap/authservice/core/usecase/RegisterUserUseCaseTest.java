package br.com.fiap.authservice.core.usecase;

import br.com.fiap.authservice.core.domain.User;
import br.com.fiap.authservice.core.gateway.IdentityProviderGateway;
import br.com.fiap.authservice.core.gateway.UserGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class RegisterUserUseCaseTest {

    private UserGateway userGateway;
    private IdentityProviderGateway identityProviderGateway;
    private RegisterUserUseCase registerUserUseCase;

    @BeforeEach
    void setUp() {
        userGateway = Mockito.mock(UserGateway.class);
        identityProviderGateway = Mockito.mock(IdentityProviderGateway.class);
        registerUserUseCase = new RegisterUserUseCase(userGateway, identityProviderGateway);
    }

    @Test
    void shouldRegisterUserAndKeepRoles() {
        // Given
        User user = new User(null, "username", "email@test.com", "First", "Last");
        String password = "password";
        String keycloakId = UUID.randomUUID().toString();
        
        List<String> roles = new ArrayList<>();
        roles.add("user");
        roles.add("manage-account");
        roles.add("view-profile");
        
        when(identityProviderGateway.createUser(eq(user), eq(password))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setRoles(roles);
            return keycloakId;
        });

        User savedUser = new User(UUID.fromString(keycloakId), "username", "email@test.com", "First", "Last");
        when(userGateway.save(any(User.class))).thenReturn(savedUser);

        // When
        User result = registerUserUseCase.execute(user, password);

        // Then
        assertNotNull(result);
        assertEquals(UUID.fromString(keycloakId), result.getId());
        assertNotNull(result.getRoles());
        assertEquals(roles, result.getRoles());
    }
}
