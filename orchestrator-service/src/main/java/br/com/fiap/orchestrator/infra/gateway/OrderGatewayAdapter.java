package br.com.fiap.orchestrator.infra.gateway;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import br.com.fiap.orchestrator.core.domain.OrderDraft;
import br.com.fiap.orchestrator.core.domain.OrderItemDraft;
import br.com.fiap.orchestrator.core.dto.requests.order.AddressRequest;
import br.com.fiap.orchestrator.core.dto.requests.order.CreateOrderRequest;
import br.com.fiap.orchestrator.core.dto.requests.order.OrderItemRequest;
import br.com.fiap.orchestrator.core.gateway.OrderGateway;
import br.com.fiap.orchestrator.infra.client.OrderFeignClient;
import br.com.fiap.orchestrator.core.dto.responses.OrderResponse;

@Component
public class OrderGatewayAdapter implements OrderGateway {

    private static final Logger logger = LoggerFactory.getLogger(OrderGatewayAdapter.class);

    private final OrderFeignClient orderFeignClient;

    public OrderGatewayAdapter(OrderFeignClient orderFeignClient) {
        this.orderFeignClient = orderFeignClient;
    }

    @Override
    public OrderResponse createOrder(OrderDraft orderDraft) {
        logger.info("Creating order for client {} with total {}", orderDraft.getClientId(), orderDraft.getTotal());
        try {
            List<OrderItemRequest> items = orderDraft.getItems()
                    .stream()
                    .map(this::toItemRequest)
                    .toList();
            logger.debug("Mapped {} order items", items.size());

            CreateOrderRequest request = new CreateOrderRequest(
                    orderDraft.getClientId(),
                    orderDraft.getCpf(),
                    orderDraft.getRestaurantId(),
                    items,
                    toAddressRequest(orderDraft),
                    orderDraft.getTotal(),
                    orderDraft.getCreatedAt()
            );
            logger.debug("Built create order request: {}", request);

            OrderResponse response = orderFeignClient.createOrder(request);
            logger.info("Order created successfully for client {}: orderId={}", orderDraft.getClientId(), response.id());
            return response;
        } catch (Exception e) {
            logger.error("Error creating order for client {}: {}", orderDraft.getClientId(), e.getMessage(), e);
            throw e;
        }
    }

    private OrderItemRequest toItemRequest(OrderItemDraft item) {
        logger.debug("Mapping order item: {}", item);
        OrderItemRequest request = new OrderItemRequest(
                item.getProductId(),
                item.getName(),
                item.getQuantity(),
                item.getPrice(),
                item.getSubtotal()
        );
        logger.debug("Mapped to request: {}", request);
        return request;
    }

    private AddressRequest toAddressRequest(OrderDraft orderDraft) {
        logger.debug("Mapping address for order draft");
        AddressRequest request = new AddressRequest(
                orderDraft.getDeliveryAddress().getStreet(),
                orderDraft.getDeliveryAddress().getNumber(),
                orderDraft.getDeliveryAddress().getCity(),
                orderDraft.getDeliveryAddress().getNeighborhood(),
                orderDraft.getDeliveryAddress().getCountry(),
                orderDraft.getDeliveryAddress().getState(),
                orderDraft.getDeliveryAddress().getZipCode()
        );
        logger.debug("Mapped address: {}", request);
        return request;
    }
}