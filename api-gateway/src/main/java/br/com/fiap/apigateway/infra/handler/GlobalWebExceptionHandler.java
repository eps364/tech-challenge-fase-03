package br.com.fiap.apigateway.infra.handler;

import java.net.ConnectException;
import java.time.Instant;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import reactor.core.publisher.Mono;

@Component
@Order(-2)
public class GlobalWebExceptionHandler implements WebExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        HttpStatus status = resolveStatus(ex);
        if (status == null) {
            return Mono.error(ex);
        }

        ServerHttpResponse response = exchange.getResponse();
        if (response.isCommitted()) {
            return Mono.error(ex);
        }

        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, "Service temporarily unavailable");
        problem.setTitle("Service Unavailable");
        problem.setProperty("timestamp", Instant.now().toString());
        problem.setInstance(java.net.URI.create(exchange.getRequest().getURI().getPath()));

        return Mono.fromCallable(() -> objectMapper.writeValueAsBytes(problem))
                .flatMap(bytes -> {
                    response.getHeaders().setContentLength(bytes.length);
                    return response.writeWith(Mono.just(
                            response.bufferFactory().wrap(bytes)));
                });
    }

    private HttpStatus resolveStatus(Throwable ex) {
        if (ex instanceof ConnectException) {
            return HttpStatus.SERVICE_UNAVAILABLE;
        }
        if (ex.getCause() instanceof ConnectException) {
            return HttpStatus.SERVICE_UNAVAILABLE;
        }
        if (ex instanceof ResponseStatusException rse
                && rse.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
            return HttpStatus.SERVICE_UNAVAILABLE;
        }
        return null;
    }
}
