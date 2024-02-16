package ru.lexp00.storage.cloud.network.common;

public class DeleteMessage extends Message{

    private final State state;
    private final String titleFile;

    public DeleteMessage(String titleFile, State state) {
        super(state);
        this.state =state;
        this.titleFile = titleFile;
    }

    @Override
    public State getState() {
        return state;
    }

    public String getTitleFile() {
        return titleFile;
    }
}
