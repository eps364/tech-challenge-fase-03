package br.com.fiap.order.infra.catalog;

import br.com.fiap.order.core.dto.ProductDTO;
import br.com.fiap.order.core.gateway.CatalogClientPort;
import org.springframework.stereotype.Component;

@Component
public class CatalogClientAdapter implements CatalogClientPort {

    private final CatalogFeignClient feignClient;

    public CatalogClientAdapter(CatalogFeignClient feignClient) {
        this.feignClient = feignClient;
    }

    @Override
    public ProductDTO getProduct(Long id) {
        return feignClient.getProduct(id);
    }
}
