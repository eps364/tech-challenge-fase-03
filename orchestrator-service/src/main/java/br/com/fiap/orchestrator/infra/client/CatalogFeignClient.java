package br.com.fiap.orchestrator.infra.client;

import br.com.fiap.orchestrator.core.dto.CatalogFoodResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${services.catalog.name}")
public interface CatalogFeignClient {

    @GetMapping("/foods/{id}")
    CatalogFoodResponse findFoodById(@PathVariable("id") String id);
}