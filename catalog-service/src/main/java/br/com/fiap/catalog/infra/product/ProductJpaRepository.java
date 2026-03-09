package br.com.fiap.catalog.infra.product;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
	List<ProductEntity> findByRestaurantId(UUID restaurantId);
}
