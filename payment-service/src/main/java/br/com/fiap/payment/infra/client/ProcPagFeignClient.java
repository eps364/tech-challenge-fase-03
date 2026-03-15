package br.com.fiap.payment.infra.client;

import java.util.Map;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.fiap.payment.infra.dto.ProcPagRequest;

@FeignClient(name = "procpagClient", url = "${app.procpag.base-url}")
public interface ProcPagFeignClient {

    @PostMapping("/requisicao")
    Map<String, Object> requestPayment(@RequestBody ProcPagRequest request);

    @GetMapping("/requisicao/{pagamentoId}")
    Map<String, Object> getPaymentStatus(@PathVariable("pagamentoId") UUID pagamentoId);
}
