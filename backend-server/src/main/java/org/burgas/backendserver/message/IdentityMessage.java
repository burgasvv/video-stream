package org.burgas.backendserver.message;

public enum IdentityMessage {

    IDENTITY_NOT_AUTHORIZED("Пользователь не может просматривать или изменять данный ресурс"),
    IDENTITY_NOT_AUTHENTICATED("Пользователь не аутентифицирован"),
    WRONG_FILE_FORMAT("Неверный формат загружаемого файла"),
    IDENTITY_NOT_FOUND("Указанный вами пользователь не найден"),
    IDENTITY_IMAGE_DELETED("Изображение пользователя успешно удалено"),
    IDENTITY_IMAGE_NOT_FOUND("У заданного пользователя отсутствует изображение");

    private final String message;

    IdentityMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
