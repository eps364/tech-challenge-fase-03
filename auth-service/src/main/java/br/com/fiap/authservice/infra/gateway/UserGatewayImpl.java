package br.com.fiap.authservice.infra.gateway;

import org.springframework.stereotype.Component;

import br.com.fiap.authservice.core.domain.User;
import br.com.fiap.authservice.core.gateway.UserGateway;
import br.com.fiap.authservice.infra.entity.UserEntity;
import br.com.fiap.authservice.infra.repository.UserRepository;

@Component
public class UserGatewayImpl implements UserGateway {

    private final UserRepository userRepository;

    public UserGatewayImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        UserEntity entity = new UserEntity(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName()
        );
        UserEntity saved = userRepository.save(entity);
        return new User(
                saved.getId(),
                saved.getUsername(),
                saved.getEmail(),
                saved.getFirstName(),
                saved.getLastName(),
                null
        );
    }
}
