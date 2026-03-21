package br.com.fiap.restaurant.core.usecase;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.fiap.restaurant.core.domain.Restaurant;
import br.com.fiap.restaurant.core.dto.RestaurantResponse;
import br.com.fiap.restaurant.core.gateway.RestaurantRepositoryPort;

public class ListRestaurantsUseCase {

    private static final Logger logger = LoggerFactory.getLogger(ListRestaurantsUseCase.class);

    private final RestaurantRepositoryPort repo;

    public ListRestaurantsUseCase(RestaurantRepositoryPort repo) {
        this.repo = repo;
    }

    public List<RestaurantResponse> execute() {
        logger.info("Listing all restaurants");
        List<RestaurantResponse> restaurants = repo.findAll().stream()
                .map(this::toResponse)
                .toList();
        logger.info("Found {} restaurants", restaurants.size());
        return restaurants;
    }

    private RestaurantResponse toResponse(Restaurant r) {
        return new RestaurantResponse(r.getId(), r.getName(), r.isActive(),
                r.getStreet(), r.getNumber(), r.getDistrict(), r.getComplement(),
                r.getCity(), r.getState(), r.getZipCode(), r.getOwners(), false);
    }
}
