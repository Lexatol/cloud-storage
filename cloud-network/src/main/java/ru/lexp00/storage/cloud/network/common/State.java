package ru.lexp00.storage.cloud.network.common;

public enum State {
    SEND_LIST_REQUEST(1),
    SEND_LIST_FILES(2),
    SEND_ADD_FOLDER_SERVER(3),
    SEND_RENAME_FOLDER_SERVER(4),
    SEND_DELETE_FILE(5);

    private final int title;

    State(int title) {
        this.title = title;
    }

    public int getTitle() {
        return title;
    }
}
