package com.example.consumer;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class QueueConsumerListener {

    private static final Logger log = LoggerFactory.getLogger(QueueConsumerListener.class);

    private final RabbitTemplate rabbitTemplate;
    private final String errorQueue;
    private final int maxAttempts;

    public QueueConsumerListener(
            RabbitTemplate rabbitTemplate,
            @Value("${app.queues.error}") String errorQueue,
            @Value("${app.retry.max-attempts}") int maxAttempts
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.errorQueue = errorQueue;
        this.maxAttempts = maxAttempts;
    }

    @RabbitListener(queues = "${app.queues.main}", containerFactory = "rabbitListenerContainerFactory")
    public void consume(Message message, Channel channel) throws IOException {
        long tag = message.getMessageProperties().getDeliveryTag();
        String body = new String(message.getBody());

        int attempt = getRetryAttempt(message); // 0 na primeira vez, depois vai subindo

        try {
            log.info("[QUEUE] attempt={} body={}", attempt + 1, body);

            // ---- SIMULAÇÃO DE FALHA ----
            if (body.contains("FAIL")) {
                throw new RuntimeException("Simulando falha no processamento");
            }
            // ----------------------------

            channel.basicAck(tag, false);
            log.info("[QUEUE] ACK OK body={}", body);

        } catch (Exception ex) {
            if (attempt + 1 >= maxAttempts) {
                // Estourou tentativas: manda pra ERROR e dá ACK na original (para parar)
                log.error("[QUEUE->ERROR] maxAttempts={} body={} err={}", maxAttempts, body, ex.getMessage(), ex);

                rabbitTemplate.send(errorQueue, message); // reenvia mantendo headers/properties
                channel.basicAck(tag, false);
            } else {
                // Ainda tem tentativas: rejeita sem requeue → vai pra DLQ (retry)
                log.warn("[QUEUE->DLQ] attempt={} body={} err={}", attempt + 1, body, ex.getMessage());
                channel.basicReject(tag, false);
            }
        }
    }

    /**
     * RabbitMQ incrementa header x-death quando a mensagem passa por DLX.
     * A gente usa o count pra saber quantas vezes ela "morreu" e voltou (tentativas).
     */
    private int getRetryAttempt(Message message) {
        Object xDeath = message.getMessageProperties().getHeaders().get("x-death");
        if (!(xDeath instanceof List<?> list) || list.isEmpty()) {
            return 0;
        }

        // O primeiro item normalmente é o mais recente; vamos somar counts por segurança
        int total = 0;
        for (Object item : list) {
            if (item instanceof Map<?, ?> death) {
                Object count = death.get("count");
                if (count instanceof Long l) total += l.intValue();
                else if (count instanceof Integer i) total += i;
            }
        }
        return total;
    }
}
