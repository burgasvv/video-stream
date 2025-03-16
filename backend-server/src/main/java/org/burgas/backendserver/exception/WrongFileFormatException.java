package org.burgas.backendserver.exception;

public class WrongFileFormatException extends RuntimeException {

    public WrongFileFormatException(String message) {
        super(message);
    }
}
