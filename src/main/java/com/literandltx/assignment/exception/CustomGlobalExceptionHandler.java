package com.literandltx.assignment.exception;

import com.literandltx.assignment.exception.custom.UserRegistrationException;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Default Global Exception Handler
     */
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(
            final Exception exception,
            final WebRequest request
    ) {
        final ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getLocalizedMessage(),
                "Unexpected error occurred."
        );

        log.info("Exception was handled: {}", exception.getMessage());

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Registration Exception Handler
     */
    @ExceptionHandler({ UserRegistrationException.class })
    public ResponseEntity<Object> handleUserRegistrationException(
            final UserRegistrationException exception,
            final WebRequest request
    ) {
        final ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                exception.getLocalizedMessage(),
                "Registration exception error occurred."
        );

        log.info("Registration exception was handled: {}", exception.getMessage());

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Validation Exception Handler
     */
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException exception,
            final HttpHeaders headers,
            final HttpStatusCode status,
            final WebRequest request
    ) {
        final List<String> errors = exception.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();
        final ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                "Validation exception error occurred.",
                errors
        );

        log.error("Validation exception occurred: {}", exception.getMessage());

        return handleExceptionInternal(exception, apiError, headers, status, request);
    }

}