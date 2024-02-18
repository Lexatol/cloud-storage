package ru.lexp00.storage.cloud.network.common;

public class DirMessage extends Message{
    private final String titleDir;
    public DirMessage(String titleDir, State state) {
        super(state);
        this.titleDir = titleDir;
    }

    public String getTitleDir() {
        return titleDir;
    }

}
