package br.com.fiap.order.infra.adapters.inbound.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.fiap.order.core.domain.PaymentEventType;
import br.com.fiap.order.core.dto.messaging.PaymentStatusEvent;
import br.com.fiap.order.core.usecase.UpdateOrderPaymentStatusUseCase;

@Component
public class PaymentStatusListener {

    private static final Logger logger = LoggerFactory.getLogger(PaymentStatusListener.class);

    private final UpdateOrderPaymentStatusUseCase updateOrderPaymentStatusUseCase;
    private final ObjectMapper objectMapper;

    public PaymentStatusListener(UpdateOrderPaymentStatusUseCase updateOrderPaymentStatusUseCase,
                                 ObjectMapper objectMapper) {
        this.updateOrderPaymentStatusUseCase = updateOrderPaymentStatusUseCase;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = "${app.kafka.topics.paymentStatus}",
            groupId = "${spring.application.name}"
    )
    public void onMessage(String message) throws Exception {
        logger.info("Received payment status message: {}", message);
        PaymentStatusEvent event = objectMapper.readValue(message, PaymentStatusEvent.class);

        PaymentEventType eventType = PaymentEventType.from(event.type()).orElse(null);
        if (eventType == null) {
            logger.warn("Unknown payment event type: {}", event.type());
            return;
        }

        logger.info("Processing payment event type {} for order {}", eventType, event.orderId());
        switch (eventType) {
            case PAYMENT_PENDING -> updateOrderPaymentStatusUseCase.markAsPendingPayment(event.orderId());
            case PAYMENT_APPROVED -> updateOrderPaymentStatusUseCase.markAsPaid(event.orderId());
            case PAYMENT_FAILED -> updateOrderPaymentStatusUseCase.markAsPaymentFailed(event.orderId());
        }
        logger.info("Payment status updated for order {}", event.orderId());
    }
}