package br.com.fiap.order.core.usecase;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.fiap.order.core.domain.Order;
import br.com.fiap.order.core.domain.OrderItem;
import br.com.fiap.order.core.domain.OrderStatus;
import br.com.fiap.order.core.dto.OrderItemRequest;
import br.com.fiap.order.core.dto.OrderItemResponse;
import br.com.fiap.order.core.dto.OrderRequest;
import br.com.fiap.order.core.dto.OrderResponse;
import br.com.fiap.order.core.dto.ProductDTO;
import br.com.fiap.order.core.gateway.CatalogClientPort;
import br.com.fiap.order.core.gateway.OrderRepositoryPort;
import br.com.fiap.order.core.gateway.RestaurantClientPort;

public class CreateOrderUseCase {

    private final OrderRepositoryPort repo;
    private final CatalogClientPort catalogClient;
    private final RestaurantClientPort restaurantClient;

    public CreateOrderUseCase(OrderRepositoryPort repo, CatalogClientPort catalogClient,
                              RestaurantClientPort restaurantClient) {
        this.repo = repo;
        this.catalogClient = catalogClient;
        this.restaurantClient = restaurantClient;
    }

    public OrderResponse execute(UUID clientId, OrderRequest req) {
        List<FieldError> errors = new ArrayList<>();

        try {
            restaurantClient.validateExists(req.restaurantId());
        } catch (RestaurantNotFoundException e) {
            errors.add(new FieldError("restaurantId", e.getMessage()));
        }

        List<OrderItem> items = resolveItems(req.restaurantId(), req.items(), errors);

        if (!errors.isEmpty()) {
            throw new OrderValidationException(errors);
        }

        BigDecimal total = items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order(
                UUID.randomUUID(), clientId, req.restaurantId(),
                items, OrderStatus.PENDING_CONFIRMATION, total, Instant.now()
        );

        Order saved = repo.save(order);
        return toResponse(saved);
    }

    private List<OrderItem> resolveItems(UUID restaurantId,
                                         List<OrderItemRequest> requests,
                                         List<FieldError> errors) {
        List<OrderItem> result = new ArrayList<>();
        for (int i = 0; i < requests.size(); i++) {
            OrderItemRequest req = requests.get(i);
            try {
                ProductDTO product = catalogClient.getProduct(req.productId());
                if (!restaurantId.equals(product.restaurantId())) {
                    errors.add(new FieldError(
                            "items[" + i + "].productId",
                            "Product does not belong to the selected restaurant"
                    ));
                    continue;
                }
                result.add(new OrderItem(req.productId(), product.name(), req.quantity(), product.price()));
            } catch (ProductNotFoundException e) {
                errors.add(new FieldError("items[" + i + "].productId", e.getMessage()));
            }
        }
        return result;
    }

    private OrderResponse toResponse(Order o) {
        List<OrderItemResponse> itemResponses = o.getItems().stream()
                .map(i -> new OrderItemResponse(i.getProductId(), i.getName(),
                        i.getQuantity(), i.getPrice(), i.getSubtotal()))
                .toList();
        return new OrderResponse(o.getId(), o.getClientId(), o.getRestaurantId(),
                itemResponses, o.getStatus(), o.getTotal(), o.getCreatedAt());
    }
}
