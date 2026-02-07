package com.example.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaErrorHandlingConfig {

    private static final Logger log =
            LoggerFactory.getLogger(KafkaErrorHandlingConfig.class);

    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<String, String> template) {

        var recoverer = new DeadLetterPublishingRecoverer(
                template,
                (ConsumerRecord<?, ?> record, Exception ex) -> {

                    log.error(
                            "[KAFKA-DLT] Enviando mensagem para DLT. " +
                                    "topic={}, partition={}, offset={}, key={}, value={}, error={}",
                            record.topic(),
                            record.partition(),
                            record.offset(),
                            record.key(),
                            record.value(),
                            ex.getMessage(),
                            ex
                    );

                    return new TopicPartition(
                            record.topic() + ".DLT",
                            record.partition()
                    );
                }
        );

        // 3 tentativas, esperando 2s entre elas
        var backOff = new FixedBackOff(2000L, 3L);

        var errorHandler = new DefaultErrorHandler(recoverer, backOff);

        // Log a cada tentativa de retry
        errorHandler.setRetryListeners((record, ex, deliveryAttempt) -> {
            log.warn(
                    "[KAFKA-RETRY] Tentativa {} falhou. topic={}, partition={}, offset={}, value={}, error={}",
                    deliveryAttempt,
                    record.topic(),
                    record.partition(),
                    record.offset(),
                    record.value(),
                    ex.getMessage()
            );
        });

        log.info("[KAFKA-ERROR-HANDLER] Configurado com backoff={}ms e maxAttempts={}",
                backOff.getInterval(),
                backOff.getMaxAttempts()
        );

        return errorHandler;
    }
}
