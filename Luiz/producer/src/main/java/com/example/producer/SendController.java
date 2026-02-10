package com.example.producer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SendController {

    private final KafkaProducerService kafkaProducer;
    private final QueueProducerService queueProducer;

    public SendController(
            KafkaProducerService kafkaProducer,
            QueueProducerService queueProducer
    ) {
        this.kafkaProducer = kafkaProducer;
        this.queueProducer = queueProducer;
    }

    @Operation(summary = "Envia uma mensagem para o Kafka")
    @PostMapping("/send-kafka")
    public ResponseEntity<String> sendKafka(
            @Parameter(description = "Mensagem a ser enviada para o tópico Kafka", example = "Olá Kafka!")
            @RequestParam("msg") String msg
    ) {
        kafkaProducer.send(msg);
        return ResponseEntity.ok("Sent to Kafka: " + msg);
    }

    @Operation(summary = "Envia uma mensagem para a fila")
    @PostMapping("/send-queue")
    public ResponseEntity<String> sendQueue(
            @Parameter(description = "Mensagem a ser enviada para a fila", example = "Olá Fila!")
            @RequestParam("msg") String msg
    ) {
        queueProducer.send(msg);
        return ResponseEntity.ok("Sent to Queue: " + msg);
    }
}
