package br.com.fiap.orquestrador.core.usecase.enviar_para_clientes;

import br.com.fiap.orquestrador.core.dto.MensagemFila;
import br.com.fiap.orquestrador.core.gateway.QueuePublisherPort;
import br.com.fiap.orquestrador.core.valueobject.RoutingKeys;

public class EnviarParaClientesUseCase {

    private final QueuePublisherPort publisher;

    public EnviarParaClientesUseCase(QueuePublisherPort publisher) {
        this.publisher = publisher;
    }

    public void execute(MensagemFila mensagem) {
        publisher.publish(RoutingKeys.ORQ_CLIENTES, mensagem);
    }
}