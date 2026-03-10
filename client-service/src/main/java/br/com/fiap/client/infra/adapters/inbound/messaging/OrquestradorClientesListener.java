package br.com.fiap.client.infra.adapters.inbound.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import br.com.fiap.client.core.dto.MensagemFila;
import br.com.fiap.client.core.gateway.QueuePublisherPort;
import br.com.fiap.client.core.usecase.processar_requisicao_orquestrador.ProcessarRequisicaoOrquestradorUseCase;
import br.com.fiap.client.infra.config.RabbitQueuesProperties;
import br.com.fiap.client.infra.messaging.mapper.MensagemMapper;

@Component
public class OrquestradorClientesListener {

    private final ProcessarRequisicaoOrquestradorUseCase useCase;
    private final QueuePublisherPort publisher;
    private final RabbitQueuesProperties props;

    public OrquestradorClientesListener(ProcessarRequisicaoOrquestradorUseCase useCase,
                                        QueuePublisherPort publisher,
                                        RabbitQueuesProperties props) {
        this.useCase = useCase;
        this.publisher = publisher;
        this.props = props;
    }

    @RabbitListener(queues = "${app.rabbit.queues.in.orquestradorClientes}")
    public void onMessage(MensagemFila msg) {
        var cmd = MensagemMapper.toCommand(msg);
        var result = useCase.execute(cmd);

        var resposta = MensagemMapper.toMensagemRetorno(result);
        publisher.publish(props.queues().out().clientesOrquestrador(), resposta);
    }
}