package br.com.fiap.order.infra.gateway;

import org.springframework.stereotype.Component;

import br.com.fiap.order.core.dto.ProductDTO;
import br.com.fiap.order.core.gateway.CatalogClientPort;
import br.com.fiap.order.core.usecase.createorder.ProductNotFoundException;
import feign.FeignException;

@Component
public class CatalogClientAdapter implements CatalogClientPort {

    private final CatalogFeignClient feignClient;

    public CatalogClientAdapter(CatalogFeignClient feignClient) {
        this.feignClient = feignClient;
    }

    @Override
    public ProductDTO getProduct(Long id) {
        try {
            return feignClient.getProduct(id);
        } catch (FeignException.NotFound e) {
            throw new ProductNotFoundException(id);
        }
    }
}