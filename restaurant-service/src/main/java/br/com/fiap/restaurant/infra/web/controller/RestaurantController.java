package br.com.fiap.restaurant.infra.web.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import br.com.fiap.restaurant.core.usecase.AddOwnerToRestaurantUseCase;import br.com.fiap.restaurant.core.usecase.CreateRestaurantUseCase;import br.com.fiap.restaurant.core.usecase.DeleteRestaurantUseCase;import br.com.fiap.restaurant.core.usecase.GetRestaurantUseCase;import br.com.fiap.restaurant.core.usecase.ListOwnedRestaurantsUseCase;import br.com.fiap.restaurant.core.usecase.ListRestaurantsUseCase;import br.com.fiap.restaurant.core.usecase.UpdateRestaurantUseCase;
@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

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
        return ResponseEntity.ok(listRestaurants.execute());
    }

    @GetMapping("/owned")
    public ResponseEntity<List<RestaurantResponse>> listOwned(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(listOwnedRestaurants.execute(extractCallerId(jwt)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(getRestaurant.execute(id));
    }

    @PostMapping
    public ResponseEntity<RestaurantResponse> create(@RequestBody RestaurantRequest request,
                                                     @AuthenticationPrincipal Jwt jwt) {
        RestaurantResponse response = createRestaurant.execute(request, extractCallerId(jwt), isAdmin(jwt));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponse> update(@PathVariable UUID id,
                                                     @RequestBody RestaurantRequest request,
                                                     @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(updateRestaurant.execute(id, request, extractCallerId(jwt), isAdmin(jwt)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id,
                                       @AuthenticationPrincipal Jwt jwt) {
        deleteRestaurant.execute(id, extractCallerId(jwt), isAdmin(jwt));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{restaurantId}/owners")
    public ResponseEntity<RestaurantResponse> addOwner(@PathVariable UUID restaurantId,
                                                       @RequestBody AddOwnerRequest request) {
        return ResponseEntity.ok(addOwner.execute(restaurantId, request.userId()));
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