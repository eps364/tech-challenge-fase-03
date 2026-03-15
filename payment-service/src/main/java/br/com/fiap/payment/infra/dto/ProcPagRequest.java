package br.com.fiap.payment.infra.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record ProcPagRequest(
        @JsonProperty("valor")
        Integer valor,

        @JsonProperty("pagamento_id")
        UUID pagamentoId,

        @JsonProperty("cliente_id")
        UUID clienteId
) {
}