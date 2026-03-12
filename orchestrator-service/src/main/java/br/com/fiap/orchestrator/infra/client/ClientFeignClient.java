package br.com.fiap.orchestrator.infra.client;

import br.com.fiap.orchestrator.core.dto.ClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${services.client.name}")
public interface ClientFeignClient {

    @GetMapping("/clients/{id}")
    ClientResponse findById(@PathVariable("id") String id);
}