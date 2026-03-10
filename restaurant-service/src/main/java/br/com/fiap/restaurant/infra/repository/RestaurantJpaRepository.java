package br.com.fiap.restaurant.infra.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.restaurant.infra.entity.RestaurantEntity;

public interface RestaurantJpaRepository extends JpaRepository<RestaurantEntity, UUID> {
}
