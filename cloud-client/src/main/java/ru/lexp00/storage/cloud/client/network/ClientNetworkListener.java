package ru.lexp00.storage.cloud.client.network;//package ru.lexp00.storage.cloud.network;

import ru.lexp00.storage.cloud.network.client.ClientNetwork;

public interface ClientNetworkListener {

    void clientOnConnect(ClientNetwork clientNetwork);
}
