package br.com.fiap.client.infra.exception;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.fiap.client.core.domain.AccessDeniedException;
import br.com.fiap.client.core.usecase.ClientAccessDeniedException;
import br.com.fiap.client.core.usecase.ClientConflictException;
import br.com.fiap.client.core.usecase.ClientNotFoundException;
import br.com.fiap.client.core.domain.ValidationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Request validation failed");
        problem.setTitle("Invalid Request");
        problem.setType(URI.create("urn:problem:client:invalid-request"));

        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        problem.setProperty("errors", errors);
        return problem;
    }

    @ExceptionHandler(ClientNotFoundException.class)
    public ProblemDetail handleNotFound(ClientNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Client Not Found");
        problem.setType(URI.create("urn:problem:client:not-found"));
        return problem;
    }

    @ExceptionHandler(ClientConflictException.class)
    public ProblemDetail handleConflict(ClientConflictException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problem.setTitle("Client Conflict");
        problem.setType(URI.create("urn:problem:client:conflict"));
        return problem;
    }

    @ExceptionHandler(ClientAccessDeniedException.class)
    public ProblemDetail handleAccessDenied(ClientAccessDeniedException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage());
        problem.setTitle("Access Denied");
        problem.setType(URI.create("urn:problem:client:access-denied"));
        return problem;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleDomainAccessDenied(AccessDeniedException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage());
        problem.setTitle("Access Denied");
        problem.setType(URI.create("urn:problem:client:access-denied"));
        return problem;
    }

    @ExceptionHandler(ValidationException.class)
    public ProblemDetail handleValidation(ValidationException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Domain validation failed");
        problem.setTitle("Validation Error");
        problem.setType(URI.create("urn:problem:client:validation-error"));

        Map<String, String> errors = new LinkedHashMap<>();
        errors.put(ex.getField(), ex.getMessage());

        problem.setProperty("errors", errors);
        return problem;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problem.setTitle("Invalid Request");
        problem.setType(URI.create("urn:problem:client:invalid-request"));
        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        problem.setTitle("Internal Server Error");
        problem.setType(URI.create("urn:problem:client:internal-error"));
        return problem;
    }
}