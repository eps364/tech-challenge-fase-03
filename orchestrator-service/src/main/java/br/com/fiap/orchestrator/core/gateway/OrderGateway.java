package br.com.fiap.orchestrator.core.gateway;

import br.com.fiap.orchestrator.core.domain.OrderDraft;

public interface OrderGateway {
    void createOrder(OrderDraft orderDraft);
}