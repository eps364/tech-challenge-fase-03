package br.com.fiap.catalog.infra.web.controller;

import br.com.fiap.catalog.core.dto.ProductRequest;
import br.com.fiap.catalog.core.dto.ProductResponse;
import br.com.fiap.catalog.core.dto.ResolveProductsRequest;
import br.com.fiap.catalog.core.dto.ResolveProductsResponse;
import br.com.fiap.catalog.core.usecase.CreateProductUseCase;
import br.com.fiap.catalog.core.usecase.DeleteProductUseCase;
import br.com.fiap.catalog.core.usecase.GetProductUseCase;
import br.com.fiap.catalog.core.usecase.ListProductsByRestaurantUseCase;
import br.com.fiap.catalog.core.usecase.ListProductsUseCase;
import br.com.fiap.catalog.core.usecase.ProductAccessDeniedException;
import br.com.fiap.catalog.core.usecase.ResolveProductsUseCase;
import br.com.fiap.catalog.core.usecase.UpdateProductUseCase;
import br.com.fiap.catalog.infra.dto.RestaurantOwnershipDTO;
import br.com.fiap.catalog.infra.gateway.RestaurantFeignClient;
import feign.FeignException;
import jakarta.validation.Valid;
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

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ListProductsUseCase listProducts;
    private final ListProductsByRestaurantUseCase listProductsByRestaurant;
    private final GetProductUseCase getProduct;
    private final CreateProductUseCase createProduct;
    private final UpdateProductUseCase updateProduct;
    private final DeleteProductUseCase deleteProduct;
    private final ResolveProductsUseCase resolveProducts;
    private final RestaurantFeignClient restaurantClient;

    public ProductController(
            ListProductsUseCase listProducts,
            ListProductsByRestaurantUseCase listProductsByRestaurant,
            GetProductUseCase getProduct,
            CreateProductUseCase createProduct,
            UpdateProductUseCase updateProduct,
            DeleteProductUseCase deleteProduct,
            ResolveProductsUseCase resolveProducts,
            RestaurantFeignClient restaurantClient) {
        this.listProducts = listProducts;
        this.listProductsByRestaurant = listProductsByRestaurant;
        this.getProduct = getProduct;
        this.createProduct = createProduct;
        this.updateProduct = updateProduct;
        this.deleteProduct = deleteProduct;
        this.resolveProducts = resolveProducts;
        this.restaurantClient = restaurantClient;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> list() {
        return ResponseEntity.ok(listProducts.execute());
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<ProductResponse>> listByRestaurant(@PathVariable UUID restaurantId) {
        return ResponseEntity.ok(listProductsByRestaurant.execute(restaurantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(getProduct.execute(id));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody ProductRequest request,
                                                  @AuthenticationPrincipal Jwt jwt) {
        validateRestaurantAccess(request.restaurantId(), jwt);
        return ResponseEntity.status(HttpStatus.CREATED).body(createProduct.execute(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable UUID id,
                                                  @RequestBody ProductRequest request,
                                                  @AuthenticationPrincipal Jwt jwt) {
        ProductResponse current = getProduct.execute(id);
        validateRestaurantAccess(current.restaurantId(), jwt);
        return ResponseEntity.ok(updateProduct.execute(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id,
                                       @AuthenticationPrincipal Jwt jwt) {
        ProductResponse current = getProduct.execute(id);
        validateRestaurantAccess(current.restaurantId(), jwt);
        deleteProduct.execute(id);
        return ResponseEntity.noContent().build();
    }

    private void validateRestaurantAccess(UUID restaurantId, Jwt jwt) {
        if (jwt == null) {
            throw new ProductAccessDeniedException("Authentication required");
        }

        Object realmAccess = jwt.getClaim("realm_access");
        if (!(realmAccess instanceof Map<?, ?> ra) || !(ra.get("roles") instanceof List<?> roles)) {
            throw new ProductAccessDeniedException("Missing roles in token");
        }

        if (roles.contains("admin") || roles.contains("system")) {
            return;
        }

        if (!roles.contains("owner")) {
            throw new ProductAccessDeniedException("Only restaurant owner or admin can modify products");
        }

        UUID userId = UUID.fromString(jwt.getSubject());

        try {
            RestaurantOwnershipDTO restaurant = restaurantClient.getRestaurant(restaurantId);
            if (restaurant.owners() == null || !restaurant.owners().contains(userId)) {
                throw new ProductAccessDeniedException("User is not an owner of this restaurant");
            }
        } catch (FeignException.NotFound e) {
            throw new ProductAccessDeniedException("Restaurant not found for the product");
        }
    }

    @PostMapping("/resolve")
    public ResponseEntity<ResolveProductsResponse> resolveProducts(
            @RequestBody @Valid ResolveProductsRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        validateRestaurantAccess(request.restaurantId(), jwt);
        return ResponseEntity.ok(resolveProducts.execute(request));
    }
}