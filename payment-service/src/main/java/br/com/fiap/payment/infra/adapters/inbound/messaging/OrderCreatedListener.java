package br.com.fiap.payment.infra.adapters.inbound.messaging;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.fiap.payment.core.dto.OrderCreatedEvent;
import br.com.fiap.payment.core.usecase.StartPaymentProcessUseCase;

@Component
public class OrderCreatedListener {

    private final StartPaymentProcessUseCase startPaymentProcessUseCase;
    private final ObjectMapper objectMapper;

    public OrderCreatedListener(StartPaymentProcessUseCase startPaymentProcessUseCase,
                                ObjectMapper objectMapper) {
        this.startPaymentProcessUseCase = startPaymentProcessUseCase;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = "${app.kafka.topics.orderCreated}",
            groupId = "${spring.application.name}"
    )
    public void onMessage(String message) throws Exception {
        OrderCreatedEvent event = objectMapper.readValue(message, OrderCreatedEvent.class);
        startPaymentProcessUseCase.execute(event);
    }
}