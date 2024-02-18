package ru.lexp00.storage.cloud.network.server;

public interface ServerListener {

    void onServerRequest(String msg);
    void onServerException(String cause);
    void onServerStopped(String msg);
    void onServerStarted(String msg);

}
