package com.vinnilmg.webfluxcourse.controller.exception;

import com.vinnilmg.webfluxcourse.service.exception.ObjectNotFoundException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(DuplicateKeyException.class)
    ResponseEntity<Mono<StandardError>> duplicateKeyException(
            DuplicateKeyException e, ServerHttpRequest request
    ) {
        return ResponseEntity.badRequest()
                .body(Mono.just(
                        StandardError.builder()
                                .timestamp(now())
                                .status(BAD_REQUEST.value())
                                .error(BAD_REQUEST.getReasonPhrase())
                                .message(verifyDupKey(e.getMessage()))
                                .path(request.getPath().toString())
                                .build()
                ));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<Mono<ValidationError>> validationError(
            WebExchangeBindException e, ServerHttpRequest request
    ) {
        ValidationError error = new ValidationError(
                now(),
                request.getPath().toString(),
                BAD_REQUEST.value(),
                "Validation Error",
                "Error on validation attributes"
        );

        // Pega field errors
        e.getBindingResult().getFieldErrors().forEach(fe -> error.addError(fe.getField(), fe.getDefaultMessage()));

        return ResponseEntity.badRequest().body(Mono.just(error));
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<Mono<StandardError>> objectNotFoundException(
            ObjectNotFoundException e, ServerHttpRequest request
    ) {
        return ResponseEntity.status(NOT_FOUND)
                .body(Mono.just(
                        StandardError.builder()
                                .timestamp(now())
                                .status(NOT_FOUND.value())
                                .error(NOT_FOUND.getReasonPhrase())
                                .message(e.getMessage())
                                .path(request.getPath().toString())
                                .build()
                ));
    }

    private String verifyDupKey(String message) {
        if (message.contains("email dup key")) {
            return "E-mail already exists.";
        }
        return "Dup key exception.";
    }

}
