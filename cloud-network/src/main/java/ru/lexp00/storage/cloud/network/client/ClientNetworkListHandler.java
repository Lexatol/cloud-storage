package ru.lexp00.storage.cloud.network.client;

import ru.lexp00.storage.cloud.network.common.FileMessage;
import ru.lexp00.storage.cloud.network.common.ListMessage;

public interface ClientNetworkListHandler  {
    void onServerListFiles(ListMessage listMessage);
    void onDownloadFileOnLocal(FileMessage fileMessage);



}
