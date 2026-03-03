package br.com.fiap.restaurant.infra.restaurante;


import br.com.fiap.restaurant.core.domain.Restaurante;
import br.com.fiap.restaurant.core.gateway.RestauranteRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class RestauranteRepositoryAdapter implements RestauranteRepositoryPort {


    @Override
    public Optional<Restaurante> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public Restaurante save(Restaurante restaurante) {
        return null;
    }
}