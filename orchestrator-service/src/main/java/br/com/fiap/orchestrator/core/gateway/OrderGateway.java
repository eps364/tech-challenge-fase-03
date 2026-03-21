package br.com.fiap.orchestrator.core.gateway;

import br.com.fiap.orchestrator.core.domain.OrderDraft;
import br.com.fiap.orchestrator.core.dto.responses.OrderResponse;

public interface OrderGateway {
    OrderResponse createOrder(OrderDraft orderDraft);
}
