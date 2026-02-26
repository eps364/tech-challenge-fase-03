package br.com.fiap.clientes.infra.messaging.consumer;

import br.com.fiap.clientes.core.dto.MensagemFila;
import br.com.fiap.clientes.core.gateway.QueuePublisherPort;
import br.com.fiap.clientes.core.usecase.processar_requisicao_orquestrador.ProcessarRequisicaoOrquestradorUseCase;
import br.com.fiap.clientes.infra.messaging.mapper.MensagemMapper;
import br.com.fiap.clientes.infra.config.RabbitQueuesProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

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