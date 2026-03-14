package br.com.fiap.order.infra.gateway;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.fiap.order.core.dto.responses.RestaurantDTO;

@FeignClient(name = "restaurant-service")
public interface RestaurantFeignClient {

    @GetMapping("/restaurants/{id}")
    RestaurantDTO getRestaurant(@PathVariable("id") UUID id);
}
