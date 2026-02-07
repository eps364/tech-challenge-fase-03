package com.example.consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumerListener {

    private final String topic;

    public KafkaConsumerListener(@Value("${app.topic}") String topic) {
        this.topic = topic;
    }

    @KafkaListener(topics = "${app.topic}", groupId = "demo-group")
    public void listen(String message) {
        if (message.contains("FAIL")) {
            throw new RuntimeException("Simulando falha no processamento");
        }
        System.out.println("[CONSUMER] message=" + message);
    }

}
