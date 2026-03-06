package br.com.fiap.orchestrator.core.domain;

import br.com.fiap.orchestrator.core.dto.PriceEvent;

import java.time.LocalDateTime;

public class Request {
    private String clientId;
    private String cpf;
    private String restaurantId;
    private String foodId;
    private Address address;
    private PriceEvent price;
    private String paymentMode;
    private LocalDateTime requestDate;
}