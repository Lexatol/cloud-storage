package ru.lexp00.storage.cloud.network.common;

public class RenameMessage extends Message{
    private final State state;

    public RenameMessage(String lastTitleFile, String newTitleFile, State state) {
        super(state);
        this.state = state;
    }

    @Override
    public State getState() {
        return state;
    }
}
