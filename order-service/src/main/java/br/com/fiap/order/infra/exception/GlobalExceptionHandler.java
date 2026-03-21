package br.com.fiap.order.infra.exception;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import feign.FeignException;

import br.com.fiap.order.core.domain.ValidationException;
import br.com.fiap.order.core.usecase.OrderNotFoundException;
import br.com.fiap.order.core.usecase.OrderValidationException;
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(OrderNotFoundException.class)
    public ProblemDetail handleOrderNotFound(OrderNotFoundException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setType(URI.create("urn:problem:order-not-found"));
        pd.setTitle("Order Not Found");
        return pd;
    }

    @ExceptionHandler(OrderValidationException.class)
    public ProblemDetail handleOrderValidation(OrderValidationException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        pd.setType(URI.create("urn:problem:order-validation"));
        pd.setTitle("Order Validation Failed");
        pd.setProperty("fields", ex.getFields());
        return pd;
    }

    @ExceptionHandler(ValidationException.class)
    public ProblemDetail handleValidation(ValidationException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        pd.setType(URI.create("urn:problem:order:invalid-request"));
        pd.setTitle("Invalid Request");
        pd.setProperty("field", ex.getField());
        return pd;
    }

    @ExceptionHandler(DataAccessException.class)
    public ProblemDetail handleDataAccessException(DataAccessException ex) {
        log.error("Database error: {}", ex.getMessage(), ex);
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "A database error occurred. Please try again later.");
        pd.setType(URI.create("urn:problem:order:database-error"));
        pd.setTitle("Database Error");
        return pd;
    }

    @ExceptionHandler(FeignException.class)
    public ProblemDetail handleFeignException(FeignException ex) {
        log.error("Service communication error: {} - {} - {}", ex.status(), ex.request().url(), ex.contentUTF8(), ex);
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_GATEWAY,
                "The service communicated with an internal component that returned an error.");
        pd.setType(URI.create("urn:problem:order:service-error"));
        pd.setTitle("Service Coordination Error");
        return pd;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred. Please contact support if the problem persists.");
        pd.setType(URI.create("urn:problem:order:internal-error"));
        pd.setTitle("Internal Server Error");
        return pd;
    }
}