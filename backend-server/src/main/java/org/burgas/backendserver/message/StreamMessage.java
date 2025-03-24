package org.burgas.backendserver.message;

public enum StreamMessage {

    STREAM_HAS_ALREADY_STARTED("Стример может вести только одну трансляцию"),
    STREAM_NOT_FOUND("Не указан идентификатор стрима");

    private final String message;

    StreamMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
