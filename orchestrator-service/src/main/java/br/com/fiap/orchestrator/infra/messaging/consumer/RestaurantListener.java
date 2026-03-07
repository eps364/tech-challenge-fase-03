package br.com.fiap.orchestrator.infra.messaging.consumer;

import br.com.fiap.orchestrator.core.dto.RequestEvent;
import br.com.fiap.orchestrator.core.usecase.enviar_para_pedidos.EnviarParaPedidosUseCase;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RestaurantListener {

    private final EnviarParaPedidosUseCase enviarParaPedidosUseCase;

    public RestaurantListener(EnviarParaPedidosUseCase enviarParaPedidosUseCase) {
        this.enviarParaPedidosUseCase = enviarParaPedidosUseCase;
    }

    @RabbitListener(queues = "restaurant-orchestrator.queue")
    public void onMessage(RequestEvent event) {

        System.out.println("Resposta do serviço de Restaurantes: " + event);

        enviarParaPedidosUseCase.execute(event);

        System.out.println("Fim do processamento da mensagem de retorno do serviço de Restaurantes: " + event);
    }
}