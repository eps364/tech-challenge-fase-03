package br.com.fiap.orquestrador.core.usecase.enviar_para_restaurantes;

import br.com.fiap.orquestrador.core.dto.MensagemFila;
import br.com.fiap.orquestrador.core.gateway.QueuePublisherPort;
import br.com.fiap.orquestrador.core.valueobject.RoutingKeys;

public class EnviarParaRestaurantesUseCase {

    private final QueuePublisherPort publisher;

    public EnviarParaRestaurantesUseCase(QueuePublisherPort publisher) {
        this.publisher = publisher;
    }

    public void execute(MensagemFila mensagem) {
        publisher.publish(RoutingKeys.ORQ_RESTAURANTES, mensagem);
    }
}