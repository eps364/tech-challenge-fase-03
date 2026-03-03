package br.com.fiap.orchestrator.core.usecase.enviar_para_restaurantes;

import br.com.fiap.orchestrator.core.dto.MensagemFila;
import br.com.fiap.orchestrator.core.gateway.QueuePublisherPort;
import br.com.fiap.orchestrator.core.valueobject.RoutingKeys;

public class EnviarParaRestaurantesUseCase {

    private final QueuePublisherPort publisher;

    public EnviarParaRestaurantesUseCase(QueuePublisherPort publisher) {
        this.publisher = publisher;
    }

    public void execute(MensagemFila mensagem) {
        publisher.publish(RoutingKeys.ORQ_RESTAURANTES, mensagem);
    }
}