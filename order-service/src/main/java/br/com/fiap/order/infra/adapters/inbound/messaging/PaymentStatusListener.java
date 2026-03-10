package br.com.fiap.order.infra.adapters.inbound.messaging;

import java.util.Map;
import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import br.com.fiap.order.core.dto.QueueMessage;
import br.com.fiap.order.core.usecase.UpdateOrderPaymentStatusUseCase;
@Component
public class PaymentStatusListener {

    private final UpdateOrderPaymentStatusUseCase updateOrderPaymentStatusUseCase;

    public PaymentStatusListener(UpdateOrderPaymentStatusUseCase updateOrderPaymentStatusUseCase) {
        this.updateOrderPaymentStatusUseCase = updateOrderPaymentStatusUseCase;
    }

    @RabbitListener(queues = "${app.rabbit.queues.in.orchestratorOrders}")
    public void onMessage(QueueMessage message) {
        if (!"PAYMENT_APPROVED".equalsIgnoreCase(message.type())
                && !"PAYMENT_PENDING".equalsIgnoreCase(message.type())) {
            return;
        }

        Map<String, Object> payload = message.payload();
        UUID orderId = UUID.fromString(String.valueOf(payload.get("orderId")));

        if ("PAYMENT_APPROVED".equalsIgnoreCase(message.type())) {
            updateOrderPaymentStatusUseCase.markAsPaid(orderId);
            return;
        }

        updateOrderPaymentStatusUseCase.markAsPendingPayment(orderId);
    }
}
