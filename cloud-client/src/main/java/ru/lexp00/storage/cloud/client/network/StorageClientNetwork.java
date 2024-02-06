package ru.lexp00.storage.cloud.client.network;

import ru.lexp00.storage.cloud.network.client.ClientNetwork;
import ru.lexp00.storage.cloud.network.client.ClientNetworkListHandler;

public class StorageClientNetwork extends ClientNetwork{

    public StorageClientNetwork(String host, int port, ClientNetworkListHandler clientNetworkListHandler) {
        super(host, port, clientNetworkListHandler);
    }
}
