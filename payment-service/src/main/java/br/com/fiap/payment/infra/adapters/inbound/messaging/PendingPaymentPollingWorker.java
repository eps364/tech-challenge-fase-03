package br.com.fiap.payment.infra.adapters.inbound.messaging;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.fiap.payment.core.usecase.PollPendingPaymentsUseCase;

@Component
public class PendingPaymentPollingWorker {

    private final PollPendingPaymentsUseCase pollPendingPaymentsUseCase;

    public PendingPaymentPollingWorker(PollPendingPaymentsUseCase pollPendingPaymentsUseCase) {
        this.pollPendingPaymentsUseCase = pollPendingPaymentsUseCase;
    }

    @Scheduled(fixedDelayString = "${app.payment.polling-delay-ms:5000}")
    public void execute() {
        pollPendingPaymentsUseCase.execute();
    }
}