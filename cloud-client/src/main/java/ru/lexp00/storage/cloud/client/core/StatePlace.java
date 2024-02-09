package ru.lexp00.storage.cloud.client.core;

public enum StatePlace {
    SERVER_FOLDER(1),
    LOCAL_FOLDER(2);
    private final int title;

    StatePlace(int title) {
        this.title = title;
    }

    public int getTitle() {
        return title;
    }
}
