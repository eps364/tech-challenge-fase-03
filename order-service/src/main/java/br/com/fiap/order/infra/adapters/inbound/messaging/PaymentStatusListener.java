package br.com.fiap.order.infra.adapters.inbound.messaging;

import java.util.Map;
import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import br.com.fiap.order.core.domain.PaymentEventType;
import br.com.fiap.order.core.dto.messaging.QueueMessage;
import br.com.fiap.order.core.usecase.UpdateOrderPaymentStatusUseCase;
@Component
public class PaymentStatusListener {

    private final UpdateOrderPaymentStatusUseCase updateOrderPaymentStatusUseCase;

    public PaymentStatusListener(UpdateOrderPaymentStatusUseCase updateOrderPaymentStatusUseCase) {
        this.updateOrderPaymentStatusUseCase = updateOrderPaymentStatusUseCase;
    }

    @RabbitListener(queues = "${app.rabbit.queues.in.orchestratorOrders}")
    public void onMessage(QueueMessage message) {
        PaymentEventType eventType = PaymentEventType.from(message.type()).orElse(null);
        if (eventType == null) {
            return;
        }

        Map<String, Object> payload = message.payload();
        UUID orderId = UUID.fromString(String.valueOf(payload.get("orderId")));

        switch (eventType) {
            case PAYMENT_APPROVED -> updateOrderPaymentStatusUseCase.markAsPaid(orderId);
            case PAYMENT_PENDING -> updateOrderPaymentStatusUseCase.markAsPendingPayment(orderId);
        }
    }
}
