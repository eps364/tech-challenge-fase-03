package br.com.fiap.orchestrator.core.usecase.enviar_para_clientes;

import br.com.fiap.orchestrator.core.dto.MensagemFila;
import br.com.fiap.orchestrator.core.gateway.QueuePublisherPort;
import br.com.fiap.orchestrator.core.valueobject.RoutingKeys;

public class EnviarParaClientesUseCase {

    private final QueuePublisherPort publisher;

    public EnviarParaClientesUseCase(QueuePublisherPort publisher) {
        this.publisher = publisher;
    }

    public void execute(MensagemFila mensagem) {
        publisher.publish(RoutingKeys.ORQ_CLIENTES, mensagem);
    }
}