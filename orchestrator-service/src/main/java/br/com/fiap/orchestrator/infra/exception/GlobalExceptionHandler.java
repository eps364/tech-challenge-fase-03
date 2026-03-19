package br.com.fiap.orchestrator.infra.exception;

import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        pd.setTitle("Invalid Request");
        pd.setType(URI.create("urn:problem:orchestrator:invalid-request")); // NOSONAR: URI.create always returns non-null for valid string
        return pd;
    }

    @ExceptionHandler(org.springframework.dao.DataAccessException.class)
    public ProblemDetail handleDataAccessException(org.springframework.dao.DataAccessException ex) {
        log.error("Database error: {}", ex.getMessage(), ex);
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "A database error occurred. Please try again later.");
        pd.setTitle("Database Error");
        pd.setType(URI.create("urn:problem:orchestrator:database-error")); // NOSONAR: URI.create always returns non-null for valid string
        return pd;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex) {
        log.error("Unexpected error in orchestrator: {}", ex.getMessage(), ex);
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected internal error occurred during orchestration.");
        problem.setTitle("Internal Server Error");
        problem.setType(URI.create("urn:problem:orchestrator:internal-error")); // NOSONAR: URI.create always returns non-null for valid string
        return problem;
    }
}
