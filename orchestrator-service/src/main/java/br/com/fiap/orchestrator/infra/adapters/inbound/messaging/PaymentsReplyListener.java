package br.com.fiap.orchestrator.infra.adapters.inbound.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import br.com.fiap.orchestrator.core.dto.QueueMessage;
import br.com.fiap.orchestrator.core.usecase.SendToOrdersUseCase;import br.com.fiap.orchestrator.core.usecase.SendToPaymentsWorkerUseCase;import br.com.fiap.orchestrator.core.valueobject.RoutingKeys;

@Component
public class PaymentsReplyListener {

    private final SendToOrdersUseCase sendToOrders;
    private final SendToPaymentsWorkerUseCase sendToPaymentsWorker;

    public PaymentsReplyListener(SendToOrdersUseCase sendToOrders,
                                 SendToPaymentsWorkerUseCase sendToPaymentsWorker) {
        this.sendToOrders = sendToOrders;
        this.sendToPaymentsWorker = sendToPaymentsWorker;
    }

    @RabbitListener(queues = RoutingKeys.PAYMENTS_ORCHESTRATOR_QUEUE)
    public void onMessage(QueueMessage message) {
        if ("PAYMENT_APPROVED".equalsIgnoreCase(message.type())) {
            sendToOrders.execute(message);
            return;
        }

        if ("PAYMENT_PENDING".equalsIgnoreCase(message.type())) {
            sendToOrders.execute(message);
            sendToPaymentsWorker.execute(message);
        }
    }
}
