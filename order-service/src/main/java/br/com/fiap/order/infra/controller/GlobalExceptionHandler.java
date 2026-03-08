package br.com.fiap.order.infra.controller;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.fiap.order.core.usecase.createorder.OrderValidationException;
import br.com.fiap.order.core.usecase.getorder.OrderNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

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

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        pd.setType(URI.create("urn:problem:internal-error"));
        pd.setTitle("Internal Server Error");
        return pd;
    }
}
