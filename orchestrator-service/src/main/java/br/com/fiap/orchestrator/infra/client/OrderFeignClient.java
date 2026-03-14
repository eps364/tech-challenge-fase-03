package br.com.fiap.orchestrator.infra.client;

import br.com.fiap.orchestrator.core.dto.requests.order.CreateOrderRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "${services.order.name}")
public interface OrderFeignClient {

    @PostMapping("/orders")
    void createOrder(@RequestBody CreateOrderRequest request);
}