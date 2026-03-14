package br.com.fiap.orchestrator.infra.gateway;

import br.com.fiap.orchestrator.core.domain.OrderDraft;
import br.com.fiap.orchestrator.core.gateway.OrderGateway;
import br.com.fiap.orchestrator.infra.client.OrderFeignClient;
import br.com.fiap.orchestrator.core.dto.requests.order.CreateOrderRequest;
import org.springframework.stereotype.Component;

@Component
public class OrderGatewayAdapter implements OrderGateway {

    private final OrderFeignClient orderFeignClient;

    public OrderGatewayAdapter(OrderFeignClient orderFeignClient) {
        this.orderFeignClient = orderFeignClient;
    }

    @Override
    public void createOrder(OrderDraft orderDraft) {
        CreateOrderRequest request = new CreateOrderRequest(
                orderDraft.getClientId(),
                orderDraft.getCpf(),
                orderDraft.getRestaurantId(),
                orderDraft.getFoodId(),
                orderDraft.getQuantity(),
                orderDraft.getDeliveryAddress().getStreet(),
                orderDraft.getDeliveryAddress().getNumber(),
                orderDraft.getDeliveryAddress().getCity(),
                orderDraft.getDeliveryAddress().getNeighborhood(),
                orderDraft.getDeliveryAddress().getCountry(),
                orderDraft.getDeliveryAddress().getState(),
                orderDraft.getDeliveryAddress().getZipCode(),
                orderDraft.getUnitPrice(),
                orderDraft.getRequestDate()
        );

        orderFeignClient.createOrder(request);
    }
}