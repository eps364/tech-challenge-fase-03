package br.com.fiap.restaurant.infra.gateway;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import br.com.fiap.restaurant.core.domain.Restaurante;
import br.com.fiap.restaurant.core.gateway.RestauranteRepositoryPort;
import br.com.fiap.restaurant.infra.entity.RestaurantEntity;
import br.com.fiap.restaurant.infra.repository.RestaurantJpaRepository;

@Repository
public class RestauranteRepositoryAdapter implements RestauranteRepositoryPort {

    private final RestaurantJpaRepository jpa;

    public RestauranteRepositoryAdapter(RestaurantJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public List<Restaurante> findAll() {
        return jpa.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<Restaurante> findById(UUID id) {
        return jpa.findById(id).map(this::toDomain);
    }

    @Override
    public Restaurante save(Restaurante r) {
        RestaurantEntity entity = new RestaurantEntity(
                r.getId(), r.getNome(), r.isAtivo(),
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

    private Restaurante toDomain(RestaurantEntity e) {
        return new Restaurante(e.getId(), e.getNome(), e.isAtivo(),
                e.getStreet(), e.getNumber(), e.getDistrict(), e.getComplement(),
                e.getCity(), e.getState(), e.getZipCode(),
                new java.util.ArrayList<>(e.getOwners()));
    }
}