package ru.lexp00.storage.cloud.client.gui;

public enum StateFolder {
    SERVER_FOLDER(1),
    LOCAL_FOLDER(2);
    private final int title;

    StateFolder(int title) {
        this.title = title;
    }

    public int getTitle() {
        return title;
    }
}
