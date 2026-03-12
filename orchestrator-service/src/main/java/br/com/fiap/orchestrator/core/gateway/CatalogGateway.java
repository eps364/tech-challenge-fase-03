package br.com.fiap.orchestrator.core.gateway;

import br.com.fiap.orchestrator.core.dto.CatalogFoodResponse;

public interface CatalogGateway {
    CatalogFoodResponse findFoodById(String foodId);
}