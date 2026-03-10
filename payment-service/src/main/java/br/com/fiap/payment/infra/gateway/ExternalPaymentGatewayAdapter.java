package br.com.fiap.payment.infra.gateway;

import java.util.Map;

import org.springframework.stereotype.Component;

import br.com.fiap.payment.core.domain.Payment;
import br.com.fiap.payment.core.gateway.ExternalPaymentGatewayPort;
import br.com.fiap.payment.infra.dto.ProcPagRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Component
public class ExternalPaymentGatewayAdapter implements ExternalPaymentGatewayPort {

    private final ProcPagFeignClient procPagFeignClient;

    public ExternalPaymentGatewayAdapter(ProcPagFeignClient procPagFeignClient) {
        this.procPagFeignClient = procPagFeignClient;
    }

    @Override
    @Retry(name = "procpag")
    @CircuitBreaker(name = "procpag", fallbackMethod = "fallbackAuthorize")
    public boolean authorize(Payment payment) {
        procPagFeignClient.requestPayment(new ProcPagRequest(
                payment.getAmount(),
                payment.getId(),
                payment.getClientId()
        ));

        Map<String, Object> statusResponse = procPagFeignClient.getPaymentStatus(payment.getId());
        Object status = statusResponse.get("status");
        if (status == null) {
            return true;
        }

        String normalizedStatus = String.valueOf(status).toUpperCase();
        return normalizedStatus.contains("APROV") || normalizedStatus.contains("APPROV")
                || normalizedStatus.contains("AUTHORIZED");
    }

    @SuppressWarnings("unused")
    private boolean fallbackAuthorize(Payment payment, Throwable throwable) {
        throw new IllegalStateException("External processor unavailable", throwable);
    }
}
