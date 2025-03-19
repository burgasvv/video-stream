package org.burgas.backendserver.exception;

public class CategoryOrImageUndefinedException extends RuntimeException {

    public CategoryOrImageUndefinedException(String message) {
        super(message);
    }
}
