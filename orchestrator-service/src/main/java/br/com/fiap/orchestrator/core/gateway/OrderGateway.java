package br.com.fiap.orchestrator.core.gateway;

import br.com.fiap.orchestrator.core.dto.CreateOrderRequest;

public interface OrderGateway {
    void createOrder(CreateOrderRequest request);
}