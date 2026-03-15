package br.com.fiap.orchestrator.infra.gateway;

import br.com.fiap.orchestrator.core.domain.Product;
import br.com.fiap.orchestrator.core.dto.requests.catalog.ResolveProductsRequest;
import br.com.fiap.orchestrator.core.dto.responses.ResolveProductsResponse;
import br.com.fiap.orchestrator.core.gateway.CatalogGateway;
import br.com.fiap.orchestrator.infra.client.CatalogFeignClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CatalogGatewayAdapter implements CatalogGateway {

    private static final Logger logger = LoggerFactory.getLogger(CatalogGatewayAdapter.class);

    private final CatalogFeignClient catalogFeignClient;

    public CatalogGatewayAdapter(CatalogFeignClient catalogFeignClient) {
        this.catalogFeignClient = catalogFeignClient;
    }

    @Override
    public Map<UUID, Product> resolveProducts(UUID restaurantId, List<UUID> productIds) {
        logger.info("Calling catalog service to resolve products for restaurant {}: {}", restaurantId, productIds);
        try {
            ResolveProductsResponse response = catalogFeignClient.resolveProducts(
                    new ResolveProductsRequest(restaurantId, productIds)
            );
            logger.info("Received response from catalog service with {} products", response.products().size());

            Map<UUID, Product> products = response.products()
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
            logger.debug("Mapped products: {}", products);
            return products;
        } catch (Exception e) {
            logger.error("Error calling catalog service for restaurant {}: {}", restaurantId, e.getMessage(), e);
            throw e;
        }
    }
}