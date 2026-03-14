package br.com.fiap.orchestrator.infra.gateway;

import br.com.fiap.orchestrator.core.domain.Product;
import br.com.fiap.orchestrator.core.dto.requests.catalog.ResolveProductsRequest;
import br.com.fiap.orchestrator.core.dto.responses.ResolveProductsResponse;
import br.com.fiap.orchestrator.core.gateway.CatalogGateway;
import br.com.fiap.orchestrator.infra.client.CatalogFeignClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CatalogGatewayAdapter implements CatalogGateway {

    private final CatalogFeignClient catalogFeignClient;

    public CatalogGatewayAdapter(CatalogFeignClient catalogFeignClient) {
        this.catalogFeignClient = catalogFeignClient;
    }

    @Override
    public Map<UUID, Product> resolveProducts(UUID restaurantId, List<UUID> productIds) {
        ResolveProductsResponse response = catalogFeignClient.resolveProducts(
                new ResolveProductsRequest(restaurantId, productIds)
        );

        return response.products()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> new Product(
                                entry.getValue().id(),
                                entry.getValue().name(),
                                entry.getValue().price(),
                                entry.getValue().restaurantId(),
                                entry.getValue().foodType()
                        )
                ));
    }
}