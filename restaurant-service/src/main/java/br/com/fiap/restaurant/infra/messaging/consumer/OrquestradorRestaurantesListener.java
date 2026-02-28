package br.com.fiap.restaurant.infra.messaging.consumer;

import br.com.fiap.restaurant.core.dto.MensagemFila;
import br.com.fiap.restaurant.core.gateway.QueuePublisherPort;
import br.com.fiap.restaurant.core.usecase.processar_requisicao_orquestrador.ProcessarRequisicaoOrquestradorUseCase;
import br.com.fiap.restaurant.infra.config.RabbitQueuesProperties;
import br.com.fiap.restaurant.infra.messaging.mapper.MensagemMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrquestradorRestaurantesListener {

    private final ProcessarRequisicaoOrquestradorUseCase useCase;
    private final QueuePublisherPort publisher;
    private final RabbitQueuesProperties props;

    public OrquestradorRestaurantesListener(ProcessarRequisicaoOrquestradorUseCase useCase,
                                            QueuePublisherPort publisher,
                                            RabbitQueuesProperties props) {
        this.useCase = useCase;
        this.publisher = publisher;
        this.props = props;
    }

    @RabbitListener(queues = "${app.rabbit.queues.in.orquestradorRestaurantes}")
    public void onMessage(MensagemFila msg) {
        var cmd = MensagemMapper.toCommand(msg);
        var result = useCase.execute(cmd);

        var resposta = MensagemMapper.toMensagemRetorno(result);
        publisher.publish(props.queues().out().restaurantesOrquestrador(), resposta);
    }
}