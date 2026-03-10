package br.com.fiap.payment.infra.adapters.outbound.messaging;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import br.com.fiap.payment.core.domain.Payment;
import br.com.fiap.payment.core.gateway.EventPublisherPort;
import br.com.fiap.payment.infra.config.RabbitQueuesProperties;

@Component
public class RabbitEventPublisher implements EventPublisherPort {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitQueuesProperties properties;

    public RabbitEventPublisher(RabbitTemplate rabbitTemplate, RabbitQueuesProperties properties) {
        this.rabbitTemplate = rabbitTemplate;
        this.properties = properties;
    }

    @Override
    public void publishPaymentApproved(Payment payment) {
        rabbitTemplate.convertAndSend(
                properties.getExchange(),
                properties.getQueues().getOut().getPaymentsOrchestrator(),
                buildMessage(payment, "PAYMENT_APPROVED", null)
        );
    }

    @Override
    public void publishPaymentPending(Payment payment, String reason) {
        rabbitTemplate.convertAndSend(
                properties.getExchange(),
                properties.getQueues().getOut().getPaymentsOrchestrator(),
                buildMessage(payment, "PAYMENT_PENDING", reason)
        );
    }

    private Map<String, Object> buildMessage(Payment payment, String type, String reason) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("orderId", payment.getOrderId().toString());
        payload.put("clientId", payment.getClientId().toString());
        payload.put("paymentId", payment.getId().toString());
        payload.put("total", payment.getAmount().toString());
        payload.put("attempts", payment.getAttempts());
        payload.put("status", payment.getStatus().name());
        if (reason != null) {
            payload.put("reason", reason);
        }

        Map<String, Object> message = new HashMap<>();
        message.put("correlationId", payment.getOrderId().toString());
        message.put("type", type);
        message.put("timestamp", Instant.now().toString());
        message.put("payload", payload);
        return message;
    }
}
