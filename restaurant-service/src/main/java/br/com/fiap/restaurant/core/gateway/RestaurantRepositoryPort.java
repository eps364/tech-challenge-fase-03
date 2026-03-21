package br.com.fiap.restaurant.core.gateway;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.fiap.restaurant.core.domain.Restaurant;

public interface RestaurantRepositoryPort {
    List<Restaurant> findAll();
    Optional<Restaurant> findById(UUID id);
    Restaurant save(Restaurant restaurant);
    void delete(UUID id);
}