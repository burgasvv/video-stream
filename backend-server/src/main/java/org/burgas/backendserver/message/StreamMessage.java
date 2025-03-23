package org.burgas.backendserver.message;

public enum StreamMessage {

    STREAM_HAS_ALREADY_STARTED("Стример может вести только одну трансляцию");

    private final String message;

    StreamMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
