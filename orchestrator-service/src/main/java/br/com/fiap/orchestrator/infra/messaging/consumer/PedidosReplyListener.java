package br.com.fiap.orquestrador.infra.messaging.consumer;

import br.com.fiap.orquestrador.core.dto.MensagemFila;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PedidosReplyListener {

    @RabbitListener(queues = "Pedidos-Orquestrador.queue")
    public void onMessage(MensagemFila mensagem) {
        // aqui você chamaria um usecase do core para tratar resposta
        // por enquanto, só log
        System.out.println("Resposta de Pedidos: " + mensagem);
    }
}