package br.com.fiap.orchestrator.infra.gateway;

import br.com.fiap.orchestrator.core.domain.OrderDraft;
import br.com.fiap.orchestrator.core.domain.OrderItemDraft;
import br.com.fiap.orchestrator.core.dto.requests.order.AddressRequest;
import br.com.fiap.orchestrator.core.dto.requests.order.CreateOrderRequest;
import br.com.fiap.orchestrator.core.dto.requests.order.OrderItemRequest;
import br.com.fiap.orchestrator.core.gateway.OrderGateway;
import br.com.fiap.orchestrator.infra.client.OrderFeignClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderGatewayAdapter implements OrderGateway {

    private final OrderFeignClient orderFeignClient;

    public OrderGatewayAdapter(OrderFeignClient orderFeignClient) {
        this.orderFeignClient = orderFeignClient;
    }

    @Override
    public void createOrder(OrderDraft orderDraft) {
        List<OrderItemRequest> items = orderDraft.getItems()
                .stream()
                .map(this::toItemRequest)
                .toList();

        CreateOrderRequest request = new CreateOrderRequest(
                orderDraft.getClientId(),
                orderDraft.getCpf(),
                orderDraft.getRestaurantId(),
                items,
                toAddressRequest(orderDraft),
                orderDraft.getTotal(),
                orderDraft.getCreatedAt()
        );

        orderFeignClient.createOrder(request);
    }

    private OrderItemRequest toItemRequest(OrderItemDraft item) {
        return new OrderItemRequest(
                item.getProductId(),
                item.getName(),
                item.getQuantity(),
                item.getPrice(),
                item.getSubtotal()
        );
    }

    private AddressRequest toAddressRequest(OrderDraft orderDraft) {
        return new AddressRequest(
                orderDraft.getDeliveryAddress().getStreet(),
                orderDraft.getDeliveryAddress().getNumber(),
                orderDraft.getDeliveryAddress().getCity(),
                orderDraft.getDeliveryAddress().getNeighborhood(),
                orderDraft.getDeliveryAddress().getCountry(),
                orderDraft.getDeliveryAddress().getState(),
                orderDraft.getDeliveryAddress().getZipCode()
        );
    }
}