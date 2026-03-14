package br.com.fiap.order.infra.gateway;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.fiap.order.core.dto.responses.ProductDTO;

@FeignClient(name = "catalog-service")
public interface CatalogFeignClient {

    @GetMapping("/products/{id}")
    ProductDTO getProduct(@PathVariable("id") Long id);
}
