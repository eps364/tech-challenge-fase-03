package br.com.fiap.orchestrator.core.usecase;

import br.com.fiap.orchestrator.core.dto.CatalogFoodResponse;
import br.com.fiap.orchestrator.core.dto.ClientResponse;
import br.com.fiap.orchestrator.core.dto.CreateOrderInput;
import br.com.fiap.orchestrator.core.dto.CreateOrderRequest;
import br.com.fiap.orchestrator.core.gateway.CatalogGateway;
import br.com.fiap.orchestrator.core.gateway.ClientGateway;
import br.com.fiap.orchestrator.core.gateway.OrderGateway;

import java.time.LocalDateTime;

public class CreateOrderOrchestrationUseCase {

    private final ClientGateway clientGateway;
    private final CatalogGateway catalogGateway;
    private final OrderGateway orderGateway;

    public CreateOrderOrchestrationUseCase(
            ClientGateway clientGateway,
            CatalogGateway catalogGateway,
            OrderGateway orderGateway
    ) {
        this.clientGateway = clientGateway;
        this.catalogGateway = catalogGateway;
        this.orderGateway = orderGateway;
    }

    public void execute(String clientId, CreateOrderInput input) {
        ClientResponse client = clientGateway.findById(clientId);
        CatalogFoodResponse food = catalogGateway.findFoodById(input.getFoodId());

        CreateOrderRequest request = new CreateOrderRequest();
        request.setClientId(clientId);
        request.setCpf(client.getCpf());
        request.setRestaurantId(input.getRestaurantId());
        request.setFoodId(input.getFoodId());
        request.setQuantity(input.getQuantity());
        request.setAddress(client.getAddress());
        request.setPrice(food.getPrice());
        request.setRequestDate(LocalDateTime.now());

        orderGateway.createOrder(request);
    }
}