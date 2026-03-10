package br.com.fiap.restaurant.core.gateway;

import java.util.UUID;

public interface KeycloakAdminPort {
    void assignOwnerRole(UUID userId);
}
