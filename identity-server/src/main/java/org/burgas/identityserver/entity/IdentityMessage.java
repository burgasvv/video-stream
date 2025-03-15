package org.burgas.identityserver.entity;

public enum IdentityMessage {

    IDENTITY_NOT_AUTHORIZED("Пользователь не может просматривать или изменять данный ресурс"),
    IDENTITY_NOT_AUTHENTICATED("Пользователь не аутентифицирован");

    private final String message;

    IdentityMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
