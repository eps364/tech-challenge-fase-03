package br.com.fiap.catalog.infra.repository;

import br.com.fiap.catalog.infra.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, UUID> {
    List<ProductEntity> findByRestaurantId(UUID restaurantId);

    List<ProductEntity> findAllByIdIn(List<UUID> ids);
}