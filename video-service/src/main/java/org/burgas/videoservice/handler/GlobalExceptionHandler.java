package org.burgas.videoservice.handler;

import org.burgas.videoservice.exception.VideoNotFoundException;
import org.burgas.videoservice.exception.WrongFileFormatException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.TEXT_PLAIN;

@RestControllerAdvice
public class GlobalExceptionHandler {

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
}
