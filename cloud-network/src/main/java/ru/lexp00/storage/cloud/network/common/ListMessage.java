package ru.lexp00.storage.cloud.network.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class ListMessage extends Message {


    private final String prefixDir = "[DIR]";
    private final String splitDelimiter = " ";
    private List<String> listFiles;

    public ListMessage(State state) {
        super(state);
    }

    public ListMessage(Path path, State state) throws IOException {
        super(state);

        listFiles = Files.list(path)
                .map(p -> {
                    if (Files.isDirectory(p)) {
                        return prefixDir + splitDelimiter + p.getFileName().toString();
                    } else {
                        return p.getFileName().toString();
                    }
                })
                .sorted()
                .collect(Collectors.toList());
    }

    public List<String> getListFiles() {
        return listFiles;
    }

    public void setListFiles(List<String> listFiles) {
        this.listFiles = listFiles;
    }
}
