package br.com.fiap.authservice.core.gateway;

import br.com.fiap.authservice.core.domain.User;

public interface UserGateway {
    User save(User user);
}
