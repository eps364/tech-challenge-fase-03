package br.com.fiap.orchestrator.infra.gateway;

import br.com.fiap.orchestrator.core.dto.CatalogFoodResponse;
import br.com.fiap.orchestrator.core.gateway.CatalogGateway;
import br.com.fiap.orchestrator.infra.client.CatalogFeignClient;
import org.springframework.stereotype.Component;

@Component
public class CatalogGatewayAdapter implements CatalogGateway {

    private final CatalogFeignClient catalogFeignClient;

    public CatalogGatewayAdapter(CatalogFeignClient catalogFeignClient) {
        this.catalogFeignClient = catalogFeignClient;
    }

    @Override
    public CatalogFoodResponse findFoodById(String foodId) {
        return catalogFeignClient.findFoodById(foodId);
    }
}