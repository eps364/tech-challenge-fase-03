package br.com.fiap.payment.infra.web.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.payment.core.dto.PaymentRequest;
import br.com.fiap.payment.core.dto.PaymentResponse;
import br.com.fiap.payment.core.usecase.GetPaymentByOrderIdUseCase;
import br.com.fiap.payment.infra.web.controller.api.PaymentAPI;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/payments")
public class PaymentController implements PaymentAPI {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    private final GetPaymentByOrderIdUseCase getPaymentByOrderIdUseCase;

    public PaymentController(GetPaymentByOrderIdUseCase getPaymentByOrderIdUseCase) {
        this.getPaymentByOrderIdUseCase = getPaymentByOrderIdUseCase;
    }

    @Override
    public ResponseEntity<PaymentResponse> getByOrderId(UUID orderId) {
        logger.info("Received get payment by order ID request: {}", orderId);
        try {
            PaymentResponse response = getPaymentByOrderIdUseCase.execute(orderId);
            logger.info("Payment retrieved for order {}", orderId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error retrieving payment for order {}: {}", orderId, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Endpoint para processar pagamento de pedido.
     */
    @Override
    public ResponseEntity<PaymentResponse> createPayment(PaymentRequest request) {
        logger.info("Received process payment request");
        // ...implementação real (placeholder por enquanto para compilar)...
        return ResponseEntity.status(HttpStatus.CREATED).body(new PaymentResponse(null, request.orderId(), null, request.amount(), null, null));
    }

    /**
     * Endpoint para consultar status de pagamento.
     */
    @Override
    public ResponseEntity<PaymentResponse> getPayment(String id) {
        logger.info("Received get payment status request for ID: {}", id);
        // ...implementação real (placeholder por enquanto para compilar)...
        return ResponseEntity.ok(new PaymentResponse(UUID.fromString(id), null, null, null, null, null));
    }
}
