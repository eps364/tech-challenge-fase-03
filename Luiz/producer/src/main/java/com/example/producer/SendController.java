package com.example.producer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SendController {

    private final KafkaProducerService producer;

    public SendController(KafkaProducerService producer) {
        this.producer = producer;
    }

    @Operation(summary = "Envia uma mensagem para o Kafka")
    @PostMapping("/send")
    public ResponseEntity<String> send(
            @Parameter(description = "Mensagem a ser enviada para o tópico Kafka", example = "Olá Kafka!")
            @RequestParam("msg") String msg
    ) {
        producer.send(msg);
        return ResponseEntity.ok("Sent: " + msg);
    }
}
