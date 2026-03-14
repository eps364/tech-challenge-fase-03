package br.com.fiap.orchestrator.infra.client;

import br.com.fiap.orchestrator.core.dto.responses.client.ClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "client-service")
public interface ClientFeignClient {

    @GetMapping("/clients/{id}")
    ClientResponse getClientById(@PathVariable("id") UUID id);
}