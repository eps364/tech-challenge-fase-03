package br.com.fiap.orchestrator.infra.gateway;

import br.com.fiap.orchestrator.core.dto.CreateOrderRequest;
import br.com.fiap.orchestrator.core.gateway.OrderGateway;
import br.com.fiap.orchestrator.infra.client.OrderFeignClient;
import org.springframework.stereotype.Component;

@Component
public class OrderGatewayAdapter implements OrderGateway {

    private final OrderFeignClient orderFeignClient;

    public OrderGatewayAdapter(OrderFeignClient orderFeignClient) {
        this.orderFeignClient = orderFeignClient;
    }

    @Override
    public void createOrder(CreateOrderRequest request) {
        orderFeignClient.createOrder(request);
    }
}