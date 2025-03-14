package org.burgas.identityserver.entity;

public enum IdentityMessage {

    IDENTITY_NOT_AUTHORIZED("Пользователь не может просматривать данный ресурс");

    private final String message;

    IdentityMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
