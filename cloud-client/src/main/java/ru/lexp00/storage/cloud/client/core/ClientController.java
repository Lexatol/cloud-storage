package ru.lexp00.storage.cloud.client.core;

import ru.lexp00.storage.cloud.client.network.ClientNetworkListener;
import ru.lexp00.storage.cloud.network.client.ClientNetwork;
import ru.lexp00.storage.cloud.network.common.*;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ClientController implements ClientNetworkListener {


    private final String prefixDir = "[DIR]";
    private final String splitDelimiter = " ";
    private final String clientDir = "ClientFiles";
    private final String dir = "./cloud-client";
    private final Path clientPath = Paths.get(dir, clientDir);

    private ClientNetwork clientNetwork;

    public void addFolder(String newTitleDir, StatePlace stateFolder) {
        if (stateFolder.equals(StatePlace.SERVER_FOLDER)) {
            send(new DirMessage(newTitleDir, State.SEND_ADD_FOLDER_SERVER));
        } else if (stateFolder.equals(StatePlace.LOCAL_FOLDER)) {
            Path path = Paths.get(dir, clientDir, newTitleDir);
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void addFile(FileMessage fileMessage) {
        Path path = Paths.get(dir, clientDir, fileMessage.getTitleFile());
        try {
            Files.write(path, fileMessage.getDataFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void renameFile(String lastTitleFile, String newTitleFile, StatePlace statePlace) {
        Path sourcePath;
        Path destinationPath = Paths.get(dir, clientDir, newTitleFile);
        if (!lastTitleFile.contains(prefixDir)) {
            sourcePath = Paths.get(dir, clientDir, lastTitleFile);
        } else {
            String[] strLasFile = lastTitleFile.split(splitDelimiter);
            sourcePath = Paths.get(dir, clientDir, strLasFile[1]);
        }
        if (statePlace.equals(StatePlace.LOCAL_FOLDER) && !Files.exists(destinationPath)) {
            try {
                Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (statePlace.equals(StatePlace.SERVER_FOLDER)) {
            send(new RenameMessage(lastTitleFile, newTitleFile, State.SEND_RENAME_FOLDER_SERVER));
        }
    }

    @Override
    public void clientOnConnect(ClientNetwork clientNetwork) {
        this.clientNetwork = clientNetwork;
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        send(new ListRequest(State.SEND_LIST_REQUEST));
    }

    public String[] updateClientFiles() {
        String[] listFiles;
        try {
            listFiles = Files.list(clientPath)
                    .map(p -> {
                        if (Files.isDirectory(p)) {
                            return prefixDir + splitDelimiter + p.getFileName().toString();
                        } else {
                            return p.getFileName().toString();
                        }
                    })
                    .sorted()
                    .toArray(String[]::new);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return listFiles;
    }


    private void send(Message msg) {
        clientNetwork.sendMessage(msg);
    }

    public void updateListFiles(JList<String> fileList, String[] listFiles) {
        fileList.removeAll();
        fileList.setListData(listFiles);
    }

    public void deleteFile(String strTitle, StatePlace statePlace) throws IOException {
        Path path;
        if (strTitle.contains(prefixDir)) {
            String[] str = strTitle.split(splitDelimiter);
            path = Paths.get(dir, clientDir, str[1]);
        } else {
            path = Paths.get(dir, clientDir, strTitle);
        }
        if (statePlace.equals(StatePlace.LOCAL_FOLDER) && Files.exists(path)) {
            Files.delete(path);
        } else if (statePlace.equals(StatePlace.SERVER_FOLDER) ) {
            send(new DeleteMessage(strTitle, State.SEND_DELETE_FILE));
        } else {
            throw new IOException("Такой файл не найден");
        }
    }

    public void addFileOnServer(String strFile) {
        Path path = Paths.get(dir, clientDir, strFile);
        try {
            send(new FileMessage(path, State.SEND_FILE));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addFileOnLocal(String strFile) {
        send(new FileRequest(strFile, State.SEND_FILE_REQUEST));
    }
}
