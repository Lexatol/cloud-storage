package ru.lexp00.storage.cloud.network.common;

public class FileRequest extends Message{

    private final State state;
    private final String strFile;
    public FileRequest(String strFile, State state) {
        super(state);
        this.state = state;
        this.strFile = strFile;
    }

    @Override
    public State getState() {
        return state;
    }

    public String getStrFile() {
        return strFile;
    }
}
