package br.com.fiap.orchestrator.core.usecase;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.fiap.orchestrator.core.domain.Client;
import br.com.fiap.orchestrator.core.domain.OrderDraft;
import br.com.fiap.orchestrator.core.domain.OrderItemDraft;
import br.com.fiap.orchestrator.core.domain.Product;
import br.com.fiap.orchestrator.core.dto.requests.orchestration.CreateOrderItemRequest;
import br.com.fiap.orchestrator.core.dto.requests.orchestration.CreateOrderRequest;
import br.com.fiap.orchestrator.core.gateway.CatalogGateway;
import br.com.fiap.orchestrator.core.gateway.ClientGateway;
import br.com.fiap.orchestrator.core.gateway.OrderGateway;

public class CreateOrderOrchestrationUseCase {

    private static final Logger logger = LoggerFactory.getLogger(CreateOrderOrchestrationUseCase.class);

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

    public br.com.fiap.orchestrator.core.dto.responses.OrderAcceptedResponse execute(UUID clientId, CreateOrderRequest input) {
        logger.info("Starting order orchestration for client {} with request: restaurantId={}, items={}", clientId, input.restaurantId(), input.items().size());
        Client client = findClient(clientId);
        List<UUID> productIds = extractProductIds(input);
        logger.info("Extracted product IDs: {}", productIds);
        Map<UUID, Product> resolvedProducts = resolveProducts(input.restaurantId(), productIds);
        logger.info("Resolved {} products from catalog", resolvedProducts.size());
        List<OrderItemDraft> items = buildOrderItems(input.items(), resolvedProducts);
        logger.info("Built {} order items", items.size());
        BigDecimal total = calculateTotal(items);
        logger.info("Calculated total: {}", total);
        OrderDraft orderDraft = buildOrderDraft(client, input, items, total);
        logger.info("Built order draft: {}", orderDraft);

        var orderResponse = orderGateway.createOrder(orderDraft);
        logger.info("Order created successfully for client {}: orderId={}", clientId, orderResponse.id());
        return new br.com.fiap.orchestrator.core.dto.responses.OrderAcceptedResponse(orderResponse.id(), "Order accepted and will be processed.");
    }

    private Client findClient(UUID clientId) {
        logger.debug("Finding client by ID: {}", clientId);
        Client client = clientGateway.findById(clientId);
        if (client == null) {
            logger.error("Client not found: {}", clientId);
            throw new IllegalArgumentException("Cliente não encontrado: " + clientId);
        }
        logger.debug("Client found: {}", client);
        return client;
    }

    private List<UUID> extractProductIds(CreateOrderRequest input) {
        logger.debug("Extracting product IDs from request");
        List<UUID> productIds = input.items()
                .stream()
                .map(CreateOrderItemRequest::productId)
                .toList();
        logger.debug("Extracted product IDs: {}", productIds);
        return productIds;
    }

    private Map<UUID, Product> resolveProducts(UUID restaurantId, List<UUID> productIds) {
        logger.debug("Resolving products for restaurant {}: {}", restaurantId, productIds);
        Map<UUID, Product> products = catalogGateway.resolveProducts(restaurantId, productIds);
        logger.debug("Resolved products: {}", products);
        return products;
    }

    private List<OrderItemDraft> buildOrderItems(List<CreateOrderItemRequest> requests,
                                                 Map<UUID, Product> resolvedProducts) {
        logger.debug("Building order items from {} requests", requests.size());
        List<OrderItemDraft> items = requests.stream()
                .map(item -> buildOrderItem(item, resolvedProducts))
                .toList();
        logger.debug("Built order items: {}", items);
        return items;
    }

    private OrderItemDraft buildOrderItem(CreateOrderItemRequest item,
                                          Map<UUID, Product> resolvedProducts) {
        logger.debug("Building order item for product {}", item.productId());
        Product product = resolvedProducts.get(item.productId());

        if (product == null) {
            logger.error("Product not found in catalog: {}", item.productId());
            throw new IllegalArgumentException("Produto não encontrado no catálogo: " + item.productId());
        }

        OrderItemDraft orderItem = new OrderItemDraft(
                product.getId(),
                product.getName(),
                item.quantity(),
                product.getPrice()
        );
        logger.debug("Built order item: {}", orderItem);
        return orderItem;
    }

    private BigDecimal calculateTotal(List<OrderItemDraft> items) {
        logger.debug("Calculating total for {} items", items.size());
        BigDecimal total = items.stream()
                .map(OrderItemDraft::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        logger.debug("Calculated total: {}", total);
        return total;
    }

    private OrderDraft buildOrderDraft(Client client,
                                       CreateOrderRequest input,
                                       List<OrderItemDraft> items,
                                       BigDecimal total) {
        logger.debug("Building order draft for client {}", client.getId());
        OrderDraft orderDraft = new OrderDraft(
                client.getId(),
                client.getCpf(),
                input.restaurantId(),
                items,
                client.getAddress(),
                total,
                Instant.now()
        );
        logger.debug("Built order draft: {}", orderDraft);
        return orderDraft;
    }
}
