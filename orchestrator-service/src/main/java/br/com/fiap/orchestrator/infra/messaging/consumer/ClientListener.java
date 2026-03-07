package br.com.fiap.orchestrator.infra.messaging.consumer;

import br.com.fiap.orchestrator.core.dto.RequestEvent;
import br.com.fiap.orchestrator.core.usecase.enviar_para_restaurantes.EnviarParaRestaurantesUseCase;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ClientListener {

    private final EnviarParaRestaurantesUseCase enviarParaRestaurantesUseCase;

    public ClientListener(EnviarParaRestaurantesUseCase enviarParaRestaurantesUseCase) {
        this.enviarParaRestaurantesUseCase = enviarParaRestaurantesUseCase;
    }

    @RabbitListener(queues = "client-orchestrator.queue")
    public void onMessage(RequestEvent event) {

        System.out.println("Resposta do serviço de Clientes: " + event);

        enviarParaRestaurantesUseCase.execute(event);

        System.out.println("Fim do processamento da mensagem de retorno do serviço de Clientes: " + event);
    }
}