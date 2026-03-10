package br.com.fiap.order.infra.gateway;

import java.util.UUID;

import org.springframework.stereotype.Component;

import br.com.fiap.order.core.gateway.RestaurantClientPort;
import br.com.fiap.order.core.usecase.createorder.RestaurantNotFoundException;
import feign.FeignException;

@Component
public class RestaurantClientAdapter implements RestaurantClientPort {

    private final RestaurantFeignClient feignClient;

    public RestaurantClientAdapter(RestaurantFeignClient feignClient) {
        this.feignClient = feignClient;
    }

    @Override
    public void validateExists(UUID restaurantId) {
        try {
            feignClient.getRestaurant(restaurantId);
        } catch (FeignException.NotFound e) {
            throw new RestaurantNotFoundException(restaurantId);
        }
    }
}