package org.burgas.backendserver.entity;

public enum StreamerMessage {

    STREAMER_NOT_FOUND("Указанный вами стример не найден"),
    STREAMER_IMAGE_NOT_FOUND("У данного стримера отсутствует изображение"),
    STREAMER_IMAGE_DELETED("Изображение стримера успешно удалено"),
    WRONG_FILE_FORMAT("Неверный формат файла");

    private final String message;

    StreamerMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
