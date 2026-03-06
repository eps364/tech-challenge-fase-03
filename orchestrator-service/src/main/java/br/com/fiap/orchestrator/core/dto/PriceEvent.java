package br.com.fiap.orchestrator.core.dto;

import java.math.BigDecimal;

public record PriceEvent(
        BigDecimal foodPrice,
        BigDecimal deliveryPrice
) {}