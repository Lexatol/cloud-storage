package ru.lexp00.storage.cloud.network.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileMessage extends Message{

    private State state;
    private String titleFile;
    private byte[] dataFile;

    public FileMessage(Path path, State state) throws IOException {
        super(state);
        this.state = state;
        titleFile = path.getFileName().toString();
        dataFile = Files.readAllBytes(path);
    }

    public FileMessage (State state) {
        super(state);

    }

    @Override
    public State getState() {
        return state;
    }

    public String getTitleFile() {
        return titleFile;
    }

    public byte[] getDataFile() {
        return dataFile;
    }


    public void setTitleFile(String titleFile) {
        this.titleFile = titleFile;
    }

    public void setDataFile(byte[] dataFile) {
        this.dataFile = dataFile;
    }
}
