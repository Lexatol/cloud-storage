package ru.lexp00.storage.cloud.network.common;

public class DirMessage extends Message{
    private String dirTitle;
    public DirMessage(State state, String dirTitle) {
        super(state);
        this.dirTitle = dirTitle;
    }

    public String getDirTitle() {
        return dirTitle;
    }

    public void setDirTitle(String dirTitle) {
        this.dirTitle = dirTitle;
    }
}
