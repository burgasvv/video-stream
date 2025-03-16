package org.burgas.backendserver.exception;

public class FileEmptyException extends RuntimeException {

    public FileEmptyException(String message) {
        super(message);
    }
}
