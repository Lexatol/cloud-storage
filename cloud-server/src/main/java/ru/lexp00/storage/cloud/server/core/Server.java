package ru.lexp00.storage.cloud.server.core;

import ru.lexp00.storage.cloud.network.server.ServerListener;
import ru.lexp00.storage.cloud.network.server.ServerNetwork;

public class Server extends ServerNetwork{

    private String serverName;
    private ServerListener serverListener;

    public Server (String serverName, int port, ServerListener serverListener){
        super(port, serverListener);
        this.serverName = serverName;
        this.serverListener = serverListener;
    }

    public String getServerName() {
        return serverName;
    }
}
