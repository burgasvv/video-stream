package org.burgas.backendserver.message;

public enum FollowUpMessage {

    ALREADY_FOLLOW("Вы уже отслеживаете стримера"),
    SUCCESSFULLY_FOLLOW("Теперь вы отслеживаете стримера"),
    STILL_NOT_FOLLOW("Вы все еще не отслеживаете стримера"),
    SUCCESSFULLY_UNFOLLOW("Вы успешно отказались отслеживать стримера");

    private final String message;

    FollowUpMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
