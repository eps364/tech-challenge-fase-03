package br.com.fiap.orchestrator.infra.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.fiap.orchestrator.core.dto.requests.order.CreateOrderRequest;

@FeignClient(name = "order-service")
public interface OrderFeignClient {

    @PostMapping("/orders/orchestrated")
    br.com.fiap.orchestrator.core.dto.responses.OrderResponse createOrder(@RequestBody CreateOrderRequest request);
}
