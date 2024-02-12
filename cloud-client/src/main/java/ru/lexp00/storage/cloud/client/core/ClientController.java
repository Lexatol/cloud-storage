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

    private final String CLIENTFILEDIR = "ClientFiles";
    private final String DIR = "./cloud-client";
    private final Path clientPath = Paths.get(DIR, CLIENTFILEDIR);

    private ClientNetwork clientNetwork;

    public void addFolder(String newTitleDir, StatePlace stateFolder) {
        if (stateFolder.equals(StatePlace.SERVER_FOLDER)) {
            send(new DirMessage(newTitleDir, State.SEND_ADD_FOLDER_SERVER));
        } else if (stateFolder.equals(StatePlace.LOCAL_FOLDER)) {
            Path path = Paths.get(DIR, CLIENTFILEDIR, newTitleDir);
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void renameFile(String lastTitleFile, String newTitleFile, StatePlace statePlace) {
        Path sourcePath;
        Path destinationPath = Paths.get(DIR, CLIENTFILEDIR, newTitleFile);
        if (!lastTitleFile.contains("[DIR]")) {
            sourcePath = Paths.get(DIR, CLIENTFILEDIR, lastTitleFile);
        } else {
            String[] strLasFile = lastTitleFile.split(" ");
            sourcePath = Paths.get(DIR, CLIENTFILEDIR, strLasFile[1]);
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
        String[] listFiles = new String[0];
        try {
            listFiles = Files.list(clientPath)
                    .map(p -> {
                        if (Files.isDirectory(p)) {
                            return "[DIR] " + p.getFileName().toString();
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

    public Path getClientPath() {
        return clientPath;
    }

    public void deleteFile(String strTitle, StatePlace statePlace) throws IOException {
        Path path;
        if (strTitle.contains("[DIR]")) {
            String[] str = strTitle.split(" ");
            path = Paths.get(DIR, CLIENTFILEDIR, str[1]);
        } else {
            path = Paths.get(DIR, CLIENTFILEDIR, strTitle);
        }
        if (statePlace.equals(StatePlace.LOCAL_FOLDER)) {
            Files.delete(path);
        } else if(statePlace.equals(StatePlace.SERVER_FOLDER)) {
            send(new DeleteMessage(strTitle, State.SEND_DELETE_FILE));
        }
    }


    public void sendFileOnServer(String strFile) {
        Path path = Paths.get(DIR, CLIENTFILEDIR, strFile);
        try {
            send(new FileMessage(path, State.SEND_FILE));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
