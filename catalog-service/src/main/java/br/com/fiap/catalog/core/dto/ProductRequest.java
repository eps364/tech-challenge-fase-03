package br.com.fiap.catalog.core.dto;

import java.math.BigDecimal;

public record ProductRequest(
        String name,
        BigDecimal price
) {}
