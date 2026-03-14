package br.com.fiap.order.core.usecase;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import br.com.fiap.order.core.domain.Order;
import br.com.fiap.order.core.domain.OrderStatus;
import br.com.fiap.order.core.dto.requests.order.OrderItemRequest;
import br.com.fiap.order.core.dto.requests.order.OrderRequest;
import br.com.fiap.order.core.dto.responses.OrderResponse;
import br.com.fiap.order.core.dto.responses.ProductDTO;
import br.com.fiap.order.core.gateway.CatalogClientPort;
import br.com.fiap.order.core.gateway.EventPublisherPort;
import br.com.fiap.order.core.gateway.OrderRepositoryPort;
import br.com.fiap.order.core.gateway.RestaurantClientPort;

class CreateOrderUseCaseTest {

    @Test
    void shouldCreateOrderAsPendingPaymentAndPublishOrderCreatedEvent() {
        InMemoryOrderRepository repository = new InMemoryOrderRepository();
        StubCatalogClient catalogClient = new StubCatalogClient();
        StubRestaurantClient restaurantClient = new StubRestaurantClient();
        SpyEventPublisher eventPublisher = new SpyEventPublisher();

        CreateOrderUseCase useCase = new CreateOrderUseCase(repository, catalogClient, restaurantClient, eventPublisher);

        UUID clientId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        OrderRequest request = new OrderRequest(
                restaurantId,
                List.of(new OrderItemRequest(10L, 2))
        );

        catalogClient.product = new ProductDTO(10L, "Burger", new BigDecimal("15.00"), restaurantId, "SANDWICH");

        OrderResponse response = useCase.execute(clientId, request);

        assertNotNull(response.id());
        assertEquals(OrderStatus.PENDING_PAYMENT, response.status());
        assertEquals(new BigDecimal("30.00"), response.total());
        assertEquals(response.id(), eventPublisher.lastPublishedOrder.getId());
        assertEquals(OrderStatus.PENDING_PAYMENT, eventPublisher.lastPublishedOrder.getStatus());
    }

    private static class InMemoryOrderRepository implements OrderRepositoryPort {
        private Order order;

        @Override
        public Order save(Order order) {
            this.order = order;
            return order;
        }

        @Override
        public Optional<Order> findById(UUID id) {
            return Optional.ofNullable(order).filter(o -> o.getId().equals(id));
        }

        @Override
        public List<Order> findByClientId(UUID clientId) {
            return List.of();
        }
    }

    private static class StubCatalogClient implements CatalogClientPort {
        private ProductDTO product;

        @Override
        public ProductDTO getProduct(Long id) {
            return product;
        }
    }

    private static class StubRestaurantClient implements RestaurantClientPort {
        @Override
        public void validateExists(UUID restaurantId) {
            // No-op for test scenario: restaurant is always considered valid.
        }
    }

    private static class SpyEventPublisher implements EventPublisherPort {
        private Order lastPublishedOrder;

        @Override
        public void publishOrderCreated(Order order) {
            this.lastPublishedOrder = order;
        }
    }
}
