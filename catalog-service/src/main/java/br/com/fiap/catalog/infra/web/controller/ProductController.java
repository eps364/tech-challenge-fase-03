package br.com.fiap.catalog.infra.web.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

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

@RestController
@RequestMapping("/products")
public class ProductController implements br.com.fiap.catalog.infra.web.controller.api.ProductAPI {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

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
        logger.info("Received list all products request");
        try {
            List<ProductResponse> products = listProducts.execute();
            logger.info("Listed {} products", products.size());
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            logger.error("Error listing products: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<ProductResponse>> listByRestaurant(@PathVariable UUID restaurantId) {
        logger.info("Received list products by restaurant request: {}", restaurantId);
        try {
            List<ProductResponse> products = listProductsByRestaurant.execute(restaurantId);
            logger.info("Listed {} products for restaurant {}", products.size(), restaurantId);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            logger.error("Error listing products for restaurant {}: {}", restaurantId, e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> get(@PathVariable UUID id) {
        logger.info("Received get product by ID request: {}", id);
        try {
            ProductResponse product = getProduct.execute(id);
            logger.info("Retrieved product: {}", product.id());
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            logger.error("Error retrieving product {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody ProductRequest request,
                                                  @AuthenticationPrincipal Jwt jwt) {
        logger.info("Received create product request for restaurant {} by user {}", request.restaurantId(), jwt != null ? jwt.getSubject() : "anonymous");
        try {
            validateRestaurantAccess(request.restaurantId(), jwt);
            ProductResponse created = createProduct.execute(request);
            logger.info("Product created: {}", created.id());
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            logger.error("Error creating product: {}", e.getMessage(), e);
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable UUID id,
                                                  @RequestBody ProductRequest request,
                                                  @AuthenticationPrincipal Jwt jwt) {
        logger.info("Received update product request for ID {} by user {}", id, jwt != null ? jwt.getSubject() : "anonymous");
        try {
            ProductResponse current = getProduct.execute(id);
            validateRestaurantAccess(current.restaurantId(), jwt);
            ProductResponse updated = updateProduct.execute(id, request);
            logger.info("Product updated: {}", updated.id());
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            logger.error("Error updating product {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id,
                                       @AuthenticationPrincipal Jwt jwt) {
        logger.info("Received delete product request for ID {} by user {}", id, jwt != null ? jwt.getSubject() : "anonymous");
        try {
            ProductResponse current = getProduct.execute(id);
            validateRestaurantAccess(current.restaurantId(), jwt);
            deleteProduct.execute(id);
            logger.info("Product deleted: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error deleting product {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    private void validateRestaurantAccess(UUID restaurantId, Jwt jwt) {
        if (jwt == null) {
            logger.warn("Access denied: authentication required for restaurant {}", restaurantId);
            throw new ProductAccessDeniedException("Authentication required");
        }

        Object realmAccess = jwt.getClaim("realm_access");
        if (!(realmAccess instanceof Map<?, ?> ra) || !(ra.get("roles") instanceof List<?> roles)) {
            logger.warn("Access denied: missing roles in token for restaurant {}", restaurantId);
            throw new ProductAccessDeniedException("Missing roles in token");
        }

        if (roles.contains("admin") || roles.contains("system")) {
            logger.debug("Access granted: admin/system role for restaurant {}", restaurantId);
            return;
        }

        if (!roles.contains("owner")) {
            logger.warn("Access denied: not owner for restaurant {}", restaurantId);
            throw new ProductAccessDeniedException("Only restaurant owner or admin can modify products");
        }

        UUID userId = UUID.fromString(jwt.getSubject());

        try {
            RestaurantOwnershipDTO restaurant = restaurantClient.getRestaurant(restaurantId);
            if (restaurant.owners() == null || !restaurant.owners().contains(userId)) {
                logger.warn("Access denied: user {} not owner of restaurant {}", userId, restaurantId);
                throw new ProductAccessDeniedException("User is not an owner of this restaurant");
            }
            logger.debug("Access granted: user {} is owner of restaurant {}", userId, restaurantId);
        } catch (FeignException.NotFound e) {
            logger.warn("Access denied: restaurant {} not found", restaurantId);
            throw new ProductAccessDeniedException("Restaurant not found for the product");
        }
    }

    @PostMapping("/resolve")
    public ResponseEntity<ResolveProductsResponse> resolveProducts(
            @RequestBody @Valid ResolveProductsRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        logger.info("Received resolve products request for restaurant {} with {} product IDs by user {}", request.restaurantId(), request.productIds().size(), jwt != null ? jwt.getSubject() : "anonymous");
        try {
            validateRestaurantAccess(request.restaurantId(), jwt);
            ResolveProductsResponse response = resolveProducts.execute(request);
            logger.info("Resolved {} products for restaurant {}", response.products().size(), request.restaurantId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error resolving products for restaurant {}: {}", request.restaurantId(), e.getMessage(), e);
            throw e;
        }
    }
}