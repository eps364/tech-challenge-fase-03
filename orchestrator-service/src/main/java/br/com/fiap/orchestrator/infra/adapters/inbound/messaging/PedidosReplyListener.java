package br.com.fiap.orchestrator.infra.adapters.inbound.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import br.com.fiap.orchestrator.core.dto.MensagemFila;

@Component
public class PedidosReplyListener {

    @RabbitListener(queues = "Pedidos-Orquestrador.queue")
    public void onMessage(MensagemFila mensagem) {
        // aqui você chamaria um usecase do core para tratar resposta
        // por enquanto, só log
        System.out.println("Resposta de Pedidos: " + mensagem);
    }
}