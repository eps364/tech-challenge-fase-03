package br.com.fiap.orchestrator.infra.exception;


import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.fiap.orchestrator.core.exception.OrchestrationInvalidRequestException;
import br.com.fiap.orchestrator.core.exception.OrchestrationNotFoundException;
import br.com.fiap.orchestrator.core.exception.OrchestrationServiceException;
import br.com.fiap.orchestrator.core.exception.OrchestrationValidationException;
import feign.FeignException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final com.fasterxml.jackson.databind.ObjectMapper OBJECT_MAPPER = new com.fasterxml.jackson.databind.ObjectMapper();

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(OrchestrationNotFoundException.class)
    public ProblemDetail handleNotFound(OrchestrationNotFoundException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setType(URI.create("urn:problem:orchestrator:not-found"));
        pd.setTitle("Resource Not Found");
        return pd;
    }

    @ExceptionHandler(OrchestrationValidationException.class)
    public ProblemDetail handleValidation(OrchestrationValidationException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        pd.setType(URI.create("urn:problem:orchestrator:validation"));
        pd.setTitle("Validation Failed");
        pd.setProperty("fields", ex.getFields());
        return pd;
    }

    @ExceptionHandler(OrchestrationInvalidRequestException.class)
    public ProblemDetail handleInvalidRequest(OrchestrationInvalidRequestException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        pd.setType(URI.create("urn:problem:orchestrator:invalid-request"));
        pd.setTitle("Invalid Request");
        pd.setProperty("field", ex.getField());
        return pd;
    }

    @ExceptionHandler(OrchestrationServiceException.class)
    public ProblemDetail handleServiceException(OrchestrationServiceException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_GATEWAY, ex.getMessage());
        pd.setType(URI.create("urn:problem:orchestrator:service-error"));
        pd.setTitle("Service Coordination Error");
        return pd;
    }

    @ExceptionHandler(DataAccessException.class)
    public ProblemDetail handleDataAccessException(DataAccessException ex) {
        log.error("Database error: {}", ex.getMessage(), ex);
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "A database error occurred. Please try again later.");
        pd.setType(URI.create("urn:problem:orchestrator:database-error"));
        pd.setTitle("Database Error");
        return pd;
    }

    @ExceptionHandler(FeignException.class)
    public ProblemDetail handleFeignException(FeignException ex) {
        String url = ex.request() != null ? String.valueOf(ex.request().url()) : "unknown";
        String content = null;
        try {
            content = ex.contentUTF8();
        } catch (Exception e) {
            // ignore
        }
        log.error("Service communication error: {} - {} - {}", ex.status(), url, (content != null ? content : "<no content>"), ex);
        if (content != null && !content.isBlank()) {
            try {
                ProblemDetail pd = OBJECT_MAPPER.readValue(content, ProblemDetail.class);
                if (ex.status() > 0) {
                    pd.setStatus(ex.status());
                }
                return pd;
            } catch (java.io.IOException | IllegalArgumentException parseEx) {
                log.warn("Failed to parse downstream ProblemDetail: {}", parseEx.getMessage());
            }
        }
        // Fallback: generic error
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_GATEWAY,
                "The orchestrator communicated with an internal component that returned an error.");
        pd.setType(URI.create("urn:problem:orchestrator:service-error"));
        pd.setTitle("Service Coordination Error");
        return pd;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred. Please contact support if the problem persists.");
        pd.setType(URI.create("urn:problem:orchestrator:internal-error"));
        pd.setTitle("Internal Server Error");
        return pd;
    }
}
