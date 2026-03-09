package br.com.fiap.catalog.infra.restaurant;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "restaurant-service")
public interface RestaurantFeignClient {

    @GetMapping("/restaurants/{id}")
    RestaurantOwnershipDTO getRestaurant(@PathVariable("id") UUID id);
}
