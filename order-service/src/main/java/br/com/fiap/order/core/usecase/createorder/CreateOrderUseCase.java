package br.com.fiap.order.core.usecase.createorder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import br.com.fiap.order.core.domain.Order;
import br.com.fiap.order.core.domain.OrderItem;
import br.com.fiap.order.core.domain.OrderStatus;
import br.com.fiap.order.core.dto.OrderItemResponse;
import br.com.fiap.order.core.dto.OrderRequest;
import br.com.fiap.order.core.dto.OrderResponse;
import br.com.fiap.order.core.dto.ProductDTO;
import br.com.fiap.order.core.gateway.CatalogClientPort;
import br.com.fiap.order.core.gateway.OrderRepositoryPort;

public class CreateOrderUseCase {

    private final OrderRepositoryPort repo;
    private final CatalogClientPort catalogClient;

    public CreateOrderUseCase(OrderRepositoryPort repo, CatalogClientPort catalogClient) {
        this.repo = repo;
        this.catalogClient = catalogClient;
    }

    public OrderResponse execute(UUID clientId, OrderRequest req) {
        List<OrderItem> items = req.items().stream()
                .map(i -> {
                    ProductDTO product = catalogClient.getProduct(i.productId());
                    return new OrderItem(i.productId(), product.name(), i.quantity(), product.price());
                })
                .toList();

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

    private OrderResponse toResponse(Order o) {
        List<OrderItemResponse> itemResponses = o.getItems().stream()
                .map(i -> new OrderItemResponse(i.getProductId(), i.getName(),
                        i.getQuantity(), i.getPrice(), i.getSubtotal()))
                .toList();
        return new OrderResponse(o.getId(), o.getClientId(), o.getRestaurantId(),
                itemResponses, o.getStatus(), o.getTotal(), o.getCreatedAt());
    }
}
