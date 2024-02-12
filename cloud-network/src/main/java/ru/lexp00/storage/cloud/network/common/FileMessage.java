package ru.lexp00.storage.cloud.network.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileMessage extends Message{

    private State state;
    private String fileTitle;
    private byte[] data;

    public FileMessage(Path path, State state) throws IOException {
        super(state);
        this.state = state;
        fileTitle = path.getFileName().toString();
        data = Files.readAllBytes(path);
    }

    public FileMessage (State state) {
        super(state);

    }

    @Override
    public State getState() {
        return state;
    }

    public String getFileTitle() {
        return fileTitle;
    }

    public byte[] getData() {
        return data;
    }


    public void setFileTitle(String fileTitle) {
        this.fileTitle = fileTitle;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
