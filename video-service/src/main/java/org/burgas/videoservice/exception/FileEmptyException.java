package org.burgas.videoservice.exception;

public class FileEmptyException extends RuntimeException {

    public FileEmptyException(String message) {
        super(message);
    }
}
