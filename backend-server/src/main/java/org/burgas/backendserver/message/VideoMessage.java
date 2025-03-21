package org.burgas.backendserver.message;

public enum VideoMessage {

    WRONG_FILE_FORMAT("Неверный формат файла при загрузке"),
    VIDEO_DELETED("Указанное вами видео успешно удалено"),
    FILE_IS_EMPTY("Загруженный вами файл пуст"),
    VIDEO_NOT_FOUND("Указанное вами видео не найдено");

    private final String message;

    VideoMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
