package br.com.fiap.order.core.usecase;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.fiap.order.core.domain.Order;
import br.com.fiap.order.core.domain.OrderItem;
import br.com.fiap.order.core.domain.OrderStatus;
import br.com.fiap.order.core.gateway.OrderRepositoryPort;
import br.com.fiap.order.core.usecase.OrderNotFoundException;
class UpdateOrderPaymentStatusUseCaseTest {

    private InMemoryOrderRepository repository;
    private UpdateOrderPaymentStatusUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = new InMemoryOrderRepository();
        useCase = new UpdateOrderPaymentStatusUseCase(repository);
    }

    @Test
    void shouldMarkOrderAsPendingPayment() {
        Order order = newOrder(OrderStatus.CONFIRMED);
        repository.save(order);

        useCase.markAsPendingPayment(order.getId());

        assertEquals(OrderStatus.PENDING_PAYMENT, repository.findById(order.getId()).orElseThrow().getStatus());
    }

    @Test
    void shouldMarkOrderAsPaid() {
        Order order = newOrder(OrderStatus.PENDING_PAYMENT);
        repository.save(order);

        useCase.markAsPaid(order.getId());

        assertEquals(OrderStatus.PAID, repository.findById(order.getId()).orElseThrow().getStatus());
    }

    @Test
    void shouldThrowWhenOrderDoesNotExist() {
        UUID missingId = UUID.randomUUID();
        assertThrows(OrderNotFoundException.class, () -> useCase.markAsPendingPayment(missingId));
    }

    private Order newOrder(OrderStatus status) {
        return new Order(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                List.of(new OrderItem(1L, "item", 1, new BigDecimal("12.00"))),
                status,
                new BigDecimal("12.00"),
                Instant.now()
        );
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
            if (order == null || !order.getId().equals(id)) {
                return Optional.empty();
            }
            return Optional.of(order);
        }

        @Override
        public List<Order> findByClientId(UUID clientId) {
            return List.of();
        }
    }
}
