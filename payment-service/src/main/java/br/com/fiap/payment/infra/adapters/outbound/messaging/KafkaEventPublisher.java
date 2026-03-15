package br.com.fiap.payment.infra.adapters.outbound.messaging;

import java.time.Instant;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.fiap.payment.core.domain.Payment;
import br.com.fiap.payment.core.dto.PaymentStatusEvent;
import br.com.fiap.payment.core.gateway.EventPublisherPort;
import br.com.fiap.payment.infra.config.KafkaTopicsProperties;

@Component
public class KafkaEventPublisher implements EventPublisherPort {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaTopicsProperties topicsProperties;
    private final ObjectMapper objectMapper;

    public KafkaEventPublisher(KafkaTemplate<String, String> kafkaTemplate,
                               KafkaTopicsProperties topicsProperties,
                               ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicsProperties = topicsProperties;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publishPaymentPending(Payment payment, String reason) {
        publish(new PaymentStatusEvent(
                "PAYMENT_PENDING",
                payment.getOrderId(),
                payment.getId(),
                payment.getStatus().name(),
                reason,
                Instant.now()
        ));
    }

    @Override
    public void publishPaymentApproved(Payment payment) {
        publish(new PaymentStatusEvent(
                "PAYMENT_APPROVED",
                payment.getOrderId(),
                payment.getId(),
                payment.getStatus().name(),
                null,
                Instant.now()
        ));
    }

    @Override
    public void publishPaymentFailed(Payment payment, String reason) {
        publish(new PaymentStatusEvent(
                "PAYMENT_FAILED",
                payment.getOrderId(),
                payment.getId(),
                payment.getStatus().name(),
                reason,
                Instant.now()
        ));
    }

    private void publish(PaymentStatusEvent event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(topicsProperties.getPaymentStatus(), event.orderId().toString(), json);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Erro ao serializar evento de status de pagamento", e);
        }
    }
}