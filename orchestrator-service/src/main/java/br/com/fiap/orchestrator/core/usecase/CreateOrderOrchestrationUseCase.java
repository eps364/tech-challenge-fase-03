package br.com.fiap.orchestrator.core.usecase;

import br.com.fiap.orchestrator.core.domain.Client;
import br.com.fiap.orchestrator.core.domain.OrderDraft;
import br.com.fiap.orchestrator.core.domain.OrderItemDraft;
import br.com.fiap.orchestrator.core.domain.Product;
import br.com.fiap.orchestrator.core.dto.requests.orchestration.CreateOrderItemRequest;
import br.com.fiap.orchestrator.core.dto.requests.orchestration.CreateOrderRequest;
import br.com.fiap.orchestrator.core.gateway.CatalogGateway;
import br.com.fiap.orchestrator.core.gateway.ClientGateway;
import br.com.fiap.orchestrator.core.gateway.OrderGateway;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CreateOrderOrchestrationUseCase {

    private final ClientGateway clientGateway;
    private final CatalogGateway catalogGateway;
    private final OrderGateway orderGateway;

    public CreateOrderOrchestrationUseCase(ClientGateway clientGateway,
                                           CatalogGateway catalogGateway,
                                           OrderGateway orderGateway) {
        this.clientGateway = clientGateway;
        this.catalogGateway = catalogGateway;
        this.orderGateway = orderGateway;
    }

    public void execute(UUID clientId, CreateOrderRequest input) {
        Client client = clientGateway.findById(clientId);

        List<UUID> productIds = input.items()
                .stream()
                .map(CreateOrderItemRequest::productId)
                .toList();

        Map<UUID, Product> resolvedProducts =
                catalogGateway.resolveProducts(input.restaurantId(), productIds);

        List<OrderItemDraft> orderItems = input.items()
                .stream()
                .map(item -> {
                    Product product = resolvedProducts.get(item.productId());

                    return new OrderItemDraft(
                            product.getId(),
                            product.getName(),
                            item.quantity(),
                            product.getPrice()
                    );
                })
                .toList();

        BigDecimal total = orderItems.stream()
                .map(OrderItemDraft::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        OrderDraft orderDraft = new OrderDraft(
                UUID.randomUUID(),
                client.getId(),
                input.restaurantId(),
                orderItems,
                total,
                Instant.now()
        );

        orderGateway.createOrder(orderDraft);
    }
}