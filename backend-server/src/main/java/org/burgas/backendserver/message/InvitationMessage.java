package org.burgas.backendserver.message;

public enum InvitationMessage {

    INVITATION_WAS_SEND("Приглашение было отправлено"),
    INVITATION_NOT_FOUND("Приглашение не было найдено"),
    INVITATION_WAS_ACCEPTED("Приглашение было принято"),
    INVITATION_WAS_DECLINED("Приглашение было отклонено"),
    WRONG_STREAM_KEY("Неверный ключ стрима введен в приглашении"),
    INVITATION_ALREADY_HANDLED("Приглашение уже обработано"),
    WRONG_INVITATION_ANSWER("Неверный ответ на приглашение");

    private final String message;

    InvitationMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
