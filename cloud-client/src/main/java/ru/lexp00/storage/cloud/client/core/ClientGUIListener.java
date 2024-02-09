package ru.lexp00.storage.cloud.client.core;


import ru.lexp00.storage.cloud.client.core.StatePlace;

public interface ClientGUIListener {
    void addFolderPath(String newTitleDir, StatePlace stateFolder);
    void onRenameFile(String lastTitleFile, String newTitleFile, StatePlace statePlace);

}
