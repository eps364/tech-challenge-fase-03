package br.com.fiap.orquestrador.core.usecase.enviar_para_pedidos;

import br.com.fiap.orquestrador.core.dto.MensagemFila;
import br.com.fiap.orquestrador.core.gateway.QueuePublisherPort;
import br.com.fiap.orquestrador.core.valueobject.RoutingKeys;

public class EnviarParaPedidosUseCase {

    private final QueuePublisherPort publisher;

    public EnviarParaPedidosUseCase(QueuePublisherPort publisher) {
        this.publisher = publisher;
    }

    public void execute(MensagemFila mensagem) {
        publisher.publish(RoutingKeys.ORQ_PEDIDOS, mensagem);
    }
}