package br.com.fiap.restaurant.core.gateway;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.fiap.restaurant.core.domain.Restaurante;

public interface RestauranteRepositoryPort {
    List<Restaurante> findAll();
    Optional<Restaurante> findById(UUID id);
    Restaurante save(Restaurante restaurante);
    void delete(UUID id);
}