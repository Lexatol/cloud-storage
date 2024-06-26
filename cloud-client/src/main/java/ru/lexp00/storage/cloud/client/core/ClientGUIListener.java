package ru.lexp00.storage.cloud.client.core;


public interface ClientGUIListener {
    void onAddFolder(String newTitleDir, StatePlace stateFolder);
    void onRenameFile(String lastTitleFile, String newTitleFile, StatePlace statePlace);
    void onDeleteFile(String strTitle, StatePlace statePlace);
    void fileUploadOnServer(String titleFile);
    void fileDownloadOnLocal(String titleFile);


}
