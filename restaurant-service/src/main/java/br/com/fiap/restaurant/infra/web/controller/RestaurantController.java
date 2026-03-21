package br.com.fiap.restaurant.infra.web.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.restaurant.core.dto.AddOwnerRequest;
import br.com.fiap.restaurant.core.dto.RestaurantRequest;
import br.com.fiap.restaurant.core.dto.RestaurantResponse;
import br.com.fiap.restaurant.core.usecase.AddOwnerToRestaurantUseCase;
import br.com.fiap.restaurant.core.usecase.CreateRestaurantUseCase;
import br.com.fiap.restaurant.core.usecase.DeleteRestaurantUseCase;
import br.com.fiap.restaurant.core.usecase.GetRestaurantUseCase;
import br.com.fiap.restaurant.core.usecase.ListOwnedRestaurantsUseCase;
import br.com.fiap.restaurant.core.usecase.ListRestaurantsUseCase;
import br.com.fiap.restaurant.core.usecase.UpdateRestaurantUseCase;
@RestController
@RequestMapping("/restaurants")
public class RestaurantController implements br.com.fiap.restaurant.infra.web.controller.api.RestaurantAPI {

    private static final Logger logger = LoggerFactory.getLogger(RestaurantController.class);

    private final ListRestaurantsUseCase listRestaurants;
    private final ListOwnedRestaurantsUseCase listOwnedRestaurants;
    private final GetRestaurantUseCase getRestaurant;
    private final CreateRestaurantUseCase createRestaurant;
    private final UpdateRestaurantUseCase updateRestaurant;
    private final DeleteRestaurantUseCase deleteRestaurant;
    private final AddOwnerToRestaurantUseCase addOwner;

    public RestaurantController(
            ListRestaurantsUseCase listRestaurants,
            ListOwnedRestaurantsUseCase listOwnedRestaurants,
            GetRestaurantUseCase getRestaurant,
            CreateRestaurantUseCase createRestaurant,
            UpdateRestaurantUseCase updateRestaurant,
            DeleteRestaurantUseCase deleteRestaurant,
            AddOwnerToRestaurantUseCase addOwner) {
        this.listRestaurants = listRestaurants;
        this.listOwnedRestaurants = listOwnedRestaurants;
        this.getRestaurant = getRestaurant;
        this.createRestaurant = createRestaurant;
        this.updateRestaurant = updateRestaurant;
        this.deleteRestaurant = deleteRestaurant;
        this.addOwner = addOwner;
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> list() {
        logger.info("Received list all restaurants request");
        try {
            List<RestaurantResponse> restaurants = listRestaurants.execute();
            logger.info("Listed {} restaurants", restaurants.size());
            return ResponseEntity.ok(restaurants);
        } catch (Exception e) {
            logger.error("Error listing restaurants: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/owned")
    public ResponseEntity<List<RestaurantResponse>> listOwned(@AuthenticationPrincipal Jwt jwt) {
        UUID callerId = extractCallerId(jwt);
        logger.info("Received list owned restaurants request by user {}", callerId);
        try {
            List<RestaurantResponse> restaurants = listOwnedRestaurants.execute(callerId);
            logger.info("Listed {} owned restaurants for user {}", restaurants.size(), callerId);
            return ResponseEntity.ok(restaurants);
        } catch (Exception e) {
            logger.error("Error listing owned restaurants for user {}: {}", callerId, e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> get(@PathVariable UUID id) {
        logger.info("Received get restaurant by ID request: {}", id);
        try {
            RestaurantResponse restaurant = getRestaurant.execute(id);
            logger.info("Retrieved restaurant: {}", restaurant.id());
            return ResponseEntity.ok(restaurant);
        } catch (Exception e) {
            logger.error("Error retrieving restaurant {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping
    public ResponseEntity<RestaurantResponse> create(@RequestBody RestaurantRequest request,
                                                     @AuthenticationPrincipal Jwt jwt) {
        UUID callerId = extractCallerId(jwt);
        boolean isAdmin = isAdmin(jwt);
        logger.info("Received create restaurant request by user {} (admin: {})", callerId, isAdmin);
        try {
            RestaurantResponse response = createRestaurant.execute(request, callerId, isAdmin);
            logger.info("Restaurant created: {}", response.id());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Error creating restaurant: {}", e.getMessage(), e);
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponse> update(@PathVariable UUID id,
                                                     @RequestBody RestaurantRequest request,
                                                     @AuthenticationPrincipal Jwt jwt) {
        UUID callerId = extractCallerId(jwt);
        boolean isAdmin = isAdmin(jwt);
        logger.info("Received update restaurant request for ID {} by user {} (admin: {})", id, callerId, isAdmin);
        try {
            RestaurantResponse response = updateRestaurant.execute(id, request, callerId, isAdmin);
            logger.info("Restaurant updated: {}", response.id());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error updating restaurant {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id,
                                       @AuthenticationPrincipal Jwt jwt) {
        UUID callerId = extractCallerId(jwt);
        boolean isAdmin = isAdmin(jwt);
        logger.info("Received delete restaurant request for ID {} by user {} (admin: {})", id, callerId, isAdmin);
        try {
            deleteRestaurant.execute(id, callerId, isAdmin);
            logger.info("Restaurant deleted: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error deleting restaurant {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @PutMapping("/{restaurantId}/owners")
    public ResponseEntity<RestaurantResponse> addOwner(@PathVariable UUID restaurantId,
                                                       @RequestBody AddOwnerRequest request) {
        logger.info("Received add owner request for restaurant {} to user {}", restaurantId, request.userId());
        try {
            RestaurantResponse response = addOwner.execute(restaurantId, request.userId());
            logger.info("Owner added to restaurant {}", restaurantId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error adding owner to restaurant {}: {}", restaurantId, e.getMessage(), e);
            throw e;
        }
    }

    private UUID extractCallerId(Jwt jwt) {
        if (jwt == null) return null;
        return UUID.fromString(jwt.getSubject());
    }

    private boolean isAdmin(Jwt jwt) {
        if (jwt == null) return false;
        Object realmAccess = jwt.getClaim("realm_access");
        if (!(realmAccess instanceof Map<?, ?> ra)) return false;
        Object roles = ra.get("roles");
        return roles instanceof List<?> roleList && roleList.contains("admin");
    }
}