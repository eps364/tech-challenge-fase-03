package br.com.fiap.orchestrator.core.usecase.enviar_para_pedidos;

import br.com.fiap.orchestrator.core.dto.RequestEvent;
import br.com.fiap.orchestrator.core.gateway.QueuePublisherPort;
import br.com.fiap.orchestrator.core.valueobject.RoutingKeys;

public class EnviarParaPedidosUseCase {

    private final QueuePublisherPort publisher;

    public EnviarParaPedidosUseCase(QueuePublisherPort publisher) {
        this.publisher = publisher;
    }

    public void execute(RequestEvent mensagem) {
        publisher.publish(RoutingKeys.ORQ_PEDIDOS, mensagem);
    }
}