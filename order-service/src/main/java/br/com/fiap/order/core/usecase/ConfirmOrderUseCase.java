package br.com.fiap.order.core.usecase;
import br.com.fiap.order.core.domain.Order;
import br.com.fiap.order.core.domain.OrderStatus;
import br.com.fiap.order.core.dto.OrderItemResponse;
import br.com.fiap.order.core.dto.OrderResponse;
import br.com.fiap.order.core.gateway.EventPublisherPort;
import br.com.fiap.order.core.gateway.OrderRepositoryPort;
import br.com.fiap.order.core.usecase.OrderNotFoundException;
import java.util.List;
import java.util.UUID;

public class ConfirmOrderUseCase {

    private final OrderRepositoryPort repo;
    private final EventPublisherPort events;

    public ConfirmOrderUseCase(OrderRepositoryPort repo, EventPublisherPort events) {
        this.repo = repo;
        this.events = events;
    }

    public OrderResponse execute(UUID orderId) {
        Order order = repo.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        Order confirmed = order.withStatus(OrderStatus.CONFIRMED);
        Order saved = repo.save(confirmed);
        events.publishOrderCreated(saved);

        List<OrderItemResponse> items = saved.getItems().stream()
                .map(i -> new OrderItemResponse(i.getProductId(), i.getName(),
                        i.getQuantity(), i.getPrice(), i.getSubtotal()))
                .toList();
        return new OrderResponse(saved.getId(), saved.getClientId(), saved.getRestaurantId(),
                items, saved.getStatus(), saved.getTotal(), saved.getCreatedAt());
    }
}
