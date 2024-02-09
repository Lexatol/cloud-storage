package ru.lexp00.storage.cloud.network.common;

public class DeleteMessage extends Message{

    private final State state;
    private String strTitle;

    public DeleteMessage(String strTitle, State state) {
        super(state);
        this.state =state;
        this.strTitle = strTitle;
    }

    @Override
    public State getState() {
        return state;
    }

    public String getStrTitle() {
        return strTitle;
    }
}
