package uol.compass.challenge3.handler;

import java.time.Instant;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.persistence.EntityExistsException;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NoSuchElementException.class)
    public ProblemDetail handleEntityNotFoundException(NoSuchElementException e) {
        logger.error("EntityNotFoundException occurred: {}", e.getMessage());
        return this.getProblemDetail(e, HttpStatus.NOT_FOUND, e.getLocalizedMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail handleIllegalStateException(IllegalStateException e) {
        logger.error("IllegalStateException occurred: {}", e.getMessage());
        return this.getProblemDetail(e, HttpStatus.UNPROCESSABLE_ENTITY, e.getLocalizedMessage());
    }

    @ExceptionHandler(EntityExistsException.class)
    public ProblemDetail handleEntityExistsException(EntityExistsException e) {
        logger.error("EntityExistsException occurred: {}", e.getMessage());
        return this.getProblemDetail(e, HttpStatus.CONFLICT, e.getLocalizedMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolationException(ConstraintViolationException e) {
        logger.error("ConstraintViolationException occurred: {}", e.getMessage());
        return this.getProblemDetail(e, HttpStatus.UNPROCESSABLE_ENTITY, e.getLocalizedMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        logger.error("MethodArgumentTypeMismatchException occurred: {}", e.getMessage());
        return this.getProblemDetail(e, HttpStatus.NOT_FOUND, "The requested endpoint does not exist.");
    }

    private ProblemDetail getProblemDetail(Exception e, HttpStatus httpStatus, String detail) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(httpStatus, detail);
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("trace", e.getStackTrace());

        return problemDetail;
    }

}
