package br.com.fiap.restaurant.infra.exception;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.fiap.restaurant.core.usecase.RestaurantAccessDeniedException;import br.com.fiap.restaurant.core.usecase.RestaurantNotFoundException;
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RestaurantNotFoundException.class)
    public ProblemDetail handleRestaurantNotFound(RestaurantNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Restaurant Not Found");
        problem.setType(URI.create("/errors/restaurant-not-found"));
        return problem;
    }

    @ExceptionHandler(RestaurantAccessDeniedException.class)
    public ProblemDetail handleAccessDenied(RestaurantAccessDeniedException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage());
        problem.setTitle("Access Denied");
        problem.setType(URI.create("/errors/access-denied"));
        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        problem.setTitle("Internal Server Error");
        problem.setType(URI.create("/errors/internal-error"));
        return problem;
    }
}