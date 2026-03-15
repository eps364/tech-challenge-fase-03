package br.com.fiap.payment.infra.gateway;

import java.util.Map;
import java.util.UUID;

import br.com.fiap.payment.infra.client.ProcPagFeignClient;
import org.springframework.stereotype.Component;

import br.com.fiap.payment.core.domain.Payment;
import br.com.fiap.payment.core.domain.ProcPagPaymentStatus;
import br.com.fiap.payment.core.gateway.ProcPagGatewayPort;
import br.com.fiap.payment.infra.dto.ProcPagRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Component
public class ProcPagGatewayAdapter implements ProcPagGatewayPort {

    private final ProcPagFeignClient procPagFeignClient;

    public ProcPagGatewayAdapter(ProcPagFeignClient procPagFeignClient) {
        this.procPagFeignClient = procPagFeignClient;
    }

    @Override
    @Retry(name = "procpag")
    @CircuitBreaker(name = "procpag", fallbackMethod = "fallbackRequestPayment")
    public void requestPayment(Payment payment) {
        procPagFeignClient.requestPayment(new ProcPagRequest(
                payment.getAmount().intValue(),
                payment.getId(),
                payment.getClientId()
        ));
    }

    @Override
    @Retry(name = "procpag")
    @CircuitBreaker(name = "procpag", fallbackMethod = "fallbackGetPaymentStatus")
    public ProcPagPaymentStatus getPaymentStatus(UUID paymentId) {
        Map<String, Object> response = procPagFeignClient.getPaymentStatus(paymentId);
        Object rawStatus = response.get("status");
        return ProcPagPaymentStatus.fromRaw(rawStatus == null ? null : String.valueOf(rawStatus));
    }

    @SuppressWarnings("unused")
    private void fallbackRequestPayment(Payment payment, Throwable throwable) {
        throw new IllegalStateException("External processor unavailable while creating payment request", throwable);
    }

    @SuppressWarnings("unused")
    private ProcPagPaymentStatus fallbackGetPaymentStatus(UUID paymentId, Throwable throwable) {
        throw new IllegalStateException("External processor unavailable while getting payment status", throwable);
    }
}