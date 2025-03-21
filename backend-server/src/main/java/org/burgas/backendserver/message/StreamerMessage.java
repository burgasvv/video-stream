package org.burgas.backendserver.message;

public enum StreamerMessage {

    STREAMER_NOT_FOUND("Указанный вами стример не найден"),
    STREAMER_IMAGE_NOT_FOUND("У данного стримера отсутствует изображение"),
    STREAMER_IMAGE_DELETED("Изображение стримера успешно удалено"),
    WRONG_FILE_FORMAT("Неверный формат файла"),
    STREAMER_CATEGORY_DATA_EMPTY("Данные о стримере и категориях не должны быть пустыми");

    private final String message;

    StreamerMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
