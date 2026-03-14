package br.com.fiap.order.core.gateway;

import br.com.fiap.order.core.dto.responses.ProductDTO;

public interface CatalogClientPort {
    ProductDTO getProduct(Long id);
}
