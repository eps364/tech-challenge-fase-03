package br.com.fiap.restaurantes.core.gateway;

import br.com.fiap.restaurantes.core.domain.Restaurante;

import java.util.Optional;
import java.util.UUID;

public interface RestauranteRepositoryPort {
    Optional<Restaurante> findById(UUID id);
    Restaurante save(Restaurante restaurante);
}