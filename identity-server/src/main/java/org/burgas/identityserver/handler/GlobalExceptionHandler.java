package org.burgas.identityserver.handler;

import org.burgas.identityserver.exception.IdentityNotAuthorizedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IdentityNotAuthorizedException.class)
    public ResponseEntity<String> handleIdentityNotAuthorizedException(IdentityNotAuthorizedException exception) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(exception.getMessage());
    }
}
