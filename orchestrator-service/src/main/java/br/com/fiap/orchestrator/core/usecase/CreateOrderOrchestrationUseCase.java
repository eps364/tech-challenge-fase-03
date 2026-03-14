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
        Client client = findClient(clientId);
        List<UUID> productIds = extractProductIds(input);
        Map<UUID, Product> resolvedProducts = resolveProducts(input.restaurantId(), productIds);
        List<OrderItemDraft> items = buildOrderItems(input.items(), resolvedProducts);
        BigDecimal total = calculateTotal(items);
        OrderDraft orderDraft = buildOrderDraft(client, input, items, total);

        orderGateway.createOrder(orderDraft);
    }

    private Client findClient(UUID clientId) {
        return clientGateway.findById(clientId);
    }

    private List<UUID> extractProductIds(CreateOrderRequest input) {
        return input.items()
                .stream()
                .map(CreateOrderItemRequest::productId)
                .toList();
    }

    private Map<UUID, Product> resolveProducts(UUID restaurantId, List<UUID> productIds) {
        return catalogGateway.resolveProducts(restaurantId, productIds);
    }

    private List<OrderItemDraft> buildOrderItems(List<CreateOrderItemRequest> requests,
                                                 Map<UUID, Product> resolvedProducts) {
        return requests.stream()
                .map(item -> buildOrderItem(item, resolvedProducts))
                .toList();
    }

    private OrderItemDraft buildOrderItem(CreateOrderItemRequest item,
                                          Map<UUID, Product> resolvedProducts) {
        Product product = resolvedProducts.get(item.productId());

        if (product == null) {
            throw new IllegalArgumentException("Produto não encontrado no catálogo: " + item.productId());
        }

        return new OrderItemDraft(
                product.getId(),
                product.getName(),
                item.quantity(),
                product.getPrice()
        );
    }

    private BigDecimal calculateTotal(List<OrderItemDraft> items) {
        return items.stream()
                .map(OrderItemDraft::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private OrderDraft buildOrderDraft(Client client,
                                       CreateOrderRequest input,
                                       List<OrderItemDraft> items,
                                       BigDecimal total) {
        return new OrderDraft(
                client.getId(),
                client.getCpf(),
                input.restaurantId(),
                items,
                client.getAddress(),
                total,
                Instant.now()
        );
    }
}
