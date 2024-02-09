package ru.lexp00.storage.cloud.network.common;

public class RenameMessage extends Message{
    private final State state;
    private final String lastTitleFile;
    private final String newTitleFile;


    public RenameMessage(String lastTitleFile, String newTitleFile, State state) {
        super(state);
        this.state = state;
        this.lastTitleFile = lastTitleFile;
        this.newTitleFile = newTitleFile;
    }

    @Override
    public State getState() {
        return state;
    }

    public String getLastTitleFile() {
        return lastTitleFile;
    }

    public String getNewTitleFile() {
        return newTitleFile;
    }

}
