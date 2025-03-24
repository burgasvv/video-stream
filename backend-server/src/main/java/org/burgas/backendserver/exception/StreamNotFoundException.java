package org.burgas.backendserver.exception;

public class StreamNotFoundException extends RuntimeException {

    public StreamNotFoundException(String message) {
        super(message);
    }
}
