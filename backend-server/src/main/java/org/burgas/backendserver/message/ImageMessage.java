package org.burgas.backendserver.message;

public enum ImageMessage {

    FILE_EMPTY_OR_NOT_FOUND("Загруженный файл пуст или отсутствует");

    private final String message;

    ImageMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
