package ru.lexp00.storage.cloud.server.core;

import ru.lexp00.storage.cloud.network.server.ServerNetwork;

public class Server extends ServerNetwork{

    private String serverName;

    public Server (String serverName, int port){
        super(port);
        this.serverName = serverName;
    }

    public String getServerName() {
        return serverName;
    }
}
