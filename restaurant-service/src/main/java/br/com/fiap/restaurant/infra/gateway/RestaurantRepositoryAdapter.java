package br.com.fiap.restaurant.infra.gateway;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import br.com.fiap.restaurant.core.domain.Restaurant;
import br.com.fiap.restaurant.core.gateway.RestaurantRepositoryPort;
import br.com.fiap.restaurant.infra.entity.RestaurantEntity;
import br.com.fiap.restaurant.infra.repository.RestaurantJpaRepository;

@Repository
public class RestaurantRepositoryAdapter implements RestaurantRepositoryPort {

    private final RestaurantJpaRepository jpa;

    public RestaurantRepositoryAdapter(RestaurantJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public List<Restaurant> findAll() {
        return jpa.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<Restaurant> findById(UUID id) {
        return jpa.findById(id).map(this::toDomain);
    }

    @Override
    public Restaurant save(Restaurant r) {
        RestaurantEntity entity = new RestaurantEntity(
                r.getId(), r.getName(), r.isActive(),
                r.getStreet(), r.getNumber(), r.getDistrict(), r.getComplement(),
                r.getCity(), r.getState(), r.getZipCode()
        );
        entity.setOwners(new java.util.HashSet<>(r.getOwners()));
        return toDomain(jpa.save(entity));
    }

    @Override
    public void delete(UUID id) {
        jpa.deleteById(id);
    }

    private Restaurant toDomain(RestaurantEntity e) {
        return new Restaurant(e.getId(), e.getName(), e.isActive(),
                e.getStreet(), e.getNumber(), e.getDistrict(), e.getComplement(),
                e.getCity(), e.getState(), e.getZipCode(),
                new java.util.ArrayList<>(e.getOwners()));
    }
}