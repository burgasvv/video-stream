package org.burgas.backendserver.entity;

public enum CategoryMessage {

    CATEGORY_IMAGE_DELETED("Изображение категории успешно удалено"),
    CATEGORY_OR_IMAGE_UNDEFINED("У заданной категории отсутствует изображение, либо отсутствует сама категория");

    private final String message;

    CategoryMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
