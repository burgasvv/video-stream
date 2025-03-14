package org.burgas.videoservice.exception;

public class WrongFileFormatException extends RuntimeException {

    public WrongFileFormatException(String message) {
        super(message);
    }
}
