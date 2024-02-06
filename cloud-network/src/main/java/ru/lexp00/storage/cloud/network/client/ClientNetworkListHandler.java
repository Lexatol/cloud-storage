package ru.lexp00.storage.cloud.network.client;

import ru.lexp00.storage.cloud.network.common.ListMessage;
import ru.lexp00.storage.cloud.network.common.Message;

import java.util.List;

public interface ClientNetworkListHandler  {
    void onServerListFiles(ListMessage listMessage);
}
