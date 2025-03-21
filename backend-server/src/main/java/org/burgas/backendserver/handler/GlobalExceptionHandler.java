package org.burgas.backendserver.handler;

import org.burgas.backendserver.exception.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FileEmptyException.class)
    public ResponseEntity<String> handleFileEmptyException(FileEmptyException exception) {
        return ResponseEntity
                .status(NOT_FOUND)
                .contentType(APPLICATION_JSON)
                .body(exception.getMessage());
    }

    @ExceptionHandler(IdentityNotAuthorizedException.class)
    public ResponseEntity<String> handleIdentityNotAuthorizedException(IdentityNotAuthorizedException exception) {
        return ResponseEntity
                .status(UNAUTHORIZED)
                .contentType(APPLICATION_JSON)
                .body(exception.getMessage());
    }

    @ExceptionHandler(VideoNotFoundException.class)
    public ResponseEntity<String> handleVideoNotFoundException(VideoNotFoundException exception) {
        return ResponseEntity
                .status(NOT_FOUND)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(exception.getMessage());
    }

    @ExceptionHandler(WrongFileFormatException.class)
    public ResponseEntity<String> handleWrongFileFormatException(WrongFileFormatException exception) {
        return ResponseEntity
                .status(NOT_ACCEPTABLE)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(exception.getMessage());
    }

    @ExceptionHandler(CategoryOrImageUndefinedException.class)
    public ResponseEntity<String> handleCategoryOrImageUndefinedException(CategoryOrImageUndefinedException exception) {
        return ResponseEntity
                .status(NOT_ACCEPTABLE)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(exception.getMessage());
    }

    @ExceptionHandler(StreamerCategoryDataEmptyException.class)
    public ResponseEntity<String> handleStreamerCategoryDataEmptyException(StreamerCategoryDataEmptyException exception) {
        return ResponseEntity
                .status(NOT_FOUND)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(exception.getMessage());
    }
}
