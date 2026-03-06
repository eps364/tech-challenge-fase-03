package br.com.fiap.orchestrator.infra.messaging.consumer;

import br.com.fiap.orchestrator.core.dto.MensagemFila;
import br.com.fiap.orchestrator.core.usecase.enviar_para_pedidos.EnviarParaPedidosUseCase;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PedidosReplyListener {

    private final EnviarParaPedidosUseCase enviarParaPedidosUseCase;

    public PedidosReplyListener(EnviarParaPedidosUseCase enviarParaPedidosUseCase) {
        this.enviarParaPedidosUseCase = enviarParaPedidosUseCase;
    }

    @RabbitListener(queues = "Pedidos-Orquestrador.queue")
    public void onMessage(MensagemFila mensagem) {
        System.out.println("Resposta de Pedidos: " + mensagem);

        enviarParaPedidosUseCase.execute();
    }
}