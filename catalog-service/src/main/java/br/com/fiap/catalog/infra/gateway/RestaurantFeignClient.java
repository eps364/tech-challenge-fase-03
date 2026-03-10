package br.com.fiap.catalog.infra.gateway;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.fiap.catalog.infra.dto.RestaurantOwnershipDTO;

@FeignClient(name = "restaurant-service")
public interface RestaurantFeignClient {

    @GetMapping("/restaurants/{id}")
    RestaurantOwnershipDTO getRestaurant(@PathVariable("id") UUID id);
}
