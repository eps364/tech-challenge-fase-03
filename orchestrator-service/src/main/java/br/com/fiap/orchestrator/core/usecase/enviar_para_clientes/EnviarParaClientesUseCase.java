package br.com.fiap.orchestrator.core.usecase.enviar_para_clientes;

import br.com.fiap.orchestrator.core.dto.RequestEvent;
import br.com.fiap.orchestrator.core.gateway.QueuePublisherPort;
import br.com.fiap.orchestrator.core.valueobject.RoutingKeys;

public class EnviarParaClientesUseCase {

    private final QueuePublisherPort publisher;

    public EnviarParaClientesUseCase(QueuePublisherPort publisher) {
        this.publisher = publisher;
    }

    public void execute(RequestEvent mensagem) {

        // Step 0 - Mapear event para dominio

        // Step 1 - Pegar token e pegar infos basicas

        // Step 2 - Setar informações basicas capturadas do token

        // Step 3 - Publicar mensagem

        publisher.publish(RoutingKeys.ORQ_CLIENTES, mensagem);
    }
}