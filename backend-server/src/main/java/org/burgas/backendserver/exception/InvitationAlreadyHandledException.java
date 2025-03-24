package org.burgas.backendserver.exception;

public class InvitationAlreadyHandledException extends RuntimeException {

    public InvitationAlreadyHandledException(String message) {
        super(message);
    }
}
