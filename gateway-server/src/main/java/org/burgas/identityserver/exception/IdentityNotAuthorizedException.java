package org.burgas.identityserver.exception;

public class IdentityNotAuthorizedException extends RuntimeException {

    public IdentityNotAuthorizedException(String message) {
        super(message);
    }
}
