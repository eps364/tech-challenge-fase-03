package br.com.fiap.orchestrator.core.gateway;

import br.com.fiap.orchestrator.core.domain.Product;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CatalogGateway {
    Map<UUID, Product> resolveProducts(UUID restaurantId, List<UUID> productIds);
}