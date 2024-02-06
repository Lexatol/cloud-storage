package ru.lexp00.storage.cloud.client.network;//package ru.lexp00.storage.cloud.network;

import ru.lexp00.storage.cloud.network.client.ClientNetwork;

public interface ClientListener {

    void clientOnConnect(ClientNetwork clientNetwork);
}
