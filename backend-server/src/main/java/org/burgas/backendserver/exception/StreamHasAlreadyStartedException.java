package org.burgas.backendserver.exception;

public class StreamHasAlreadyStartedException extends RuntimeException {

    public StreamHasAlreadyStartedException(String message) {
        super(message);
    }
}
