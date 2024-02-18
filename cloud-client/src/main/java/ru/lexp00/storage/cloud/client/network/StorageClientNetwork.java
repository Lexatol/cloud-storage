package ru.lexp00.storage.cloud.client.network;

import ru.lexp00.storage.cloud.network.client.ClientNetwork;
import ru.lexp00.storage.cloud.network.client.ClientListener;

public class StorageClientNetwork extends ClientNetwork{

    public StorageClientNetwork(String host, int port, ClientListener clientListener) {
        super(host, port, clientListener);
    }
}
