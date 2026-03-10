package br.com.fiap.payment.infra.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProcPagRequest(
        BigDecimal valor,
        UUID pagamento_id,
        UUID cliente_id
) {}
