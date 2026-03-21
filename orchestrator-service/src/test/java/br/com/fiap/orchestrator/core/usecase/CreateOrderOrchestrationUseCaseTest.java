package br.com.fiap.orchestrator.core.usecase;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.fiap.orchestrator.core.domain.Client;
import br.com.fiap.orchestrator.core.domain.OrderDraft;
import br.com.fiap.orchestrator.core.domain.Product;
import br.com.fiap.orchestrator.core.dto.requests.orchestration.CreateOrderItemRequest;
import br.com.fiap.orchestrator.core.dto.requests.orchestration.CreateOrderRequest;
import br.com.fiap.orchestrator.core.gateway.CatalogGateway;
import br.com.fiap.orchestrator.core.gateway.ClientGateway;
import br.com.fiap.orchestrator.core.gateway.OrderGateway;

class CreateOrderOrchestrationUseCaseTest {

    @Mock
    private ClientGateway clientGateway;
    @Mock
    private CatalogGateway catalogGateway;
    @Mock
    private OrderGateway orderGateway;

    private CreateOrderOrchestrationUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new CreateOrderOrchestrationUseCase(clientGateway, catalogGateway, orderGateway);
    }

    @Test
    void execute_shouldCreateOrderWithValidItemsAndTotal() {
        UUID clientId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        Client client = mock(Client.class);
        Product product = mock(Product.class);
        when(clientGateway.findById(clientId)).thenReturn(client);
        when(product.getId()).thenReturn(productId);
        when(product.getName()).thenReturn("Product");
        when(product.getPrice()).thenReturn(BigDecimal.valueOf(10));
        when(catalogGateway.resolveProducts(restaurantId, List.of(productId))).thenReturn(Map.of(productId, product));
        CreateOrderItemRequest itemRequest = new CreateOrderItemRequest(productId, 2);
        CreateOrderRequest orderRequest = new CreateOrderRequest(restaurantId, List.of(itemRequest));

        useCase.execute(clientId, orderRequest);

        verify(orderGateway, times(1)).createOrder(any(OrderDraft.class));
    }

    @Test
    void execute_shouldThrowIfClientNotFound() {
        UUID clientId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        when(clientGateway.findById(clientId)).thenReturn(null);
        CreateOrderRequest orderRequest = new CreateOrderRequest(restaurantId, List.of());

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(clientId, orderRequest));
    }

    @Test
    void execute_shouldThrowIfProductNotFound() {
        UUID clientId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        Client client = mock(Client.class);
        when(clientGateway.findById(clientId)).thenReturn(client);
        when(catalogGateway.resolveProducts(restaurantId, List.of(productId))).thenReturn(Collections.emptyMap());
        CreateOrderItemRequest itemRequest = new CreateOrderItemRequest(productId, 1);
        CreateOrderRequest orderRequest = new CreateOrderRequest(restaurantId, List.of(itemRequest));

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(clientId, orderRequest));
    }
}
