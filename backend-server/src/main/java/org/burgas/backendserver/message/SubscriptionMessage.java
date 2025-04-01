package org.burgas.backendserver.message;

public enum SubscriptionMessage {

    SUBSCRIPTION_SAVED("Подписка прошла успешно или обновлена"),
    SUBSCRIPTION_DELETED("Подписка успешно удалена"),
    SUBSCRIPTION_NOT_FOUND("Подписка не была найдена");

    private final String message;

    SubscriptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
