package com.project.mqtt.mqtt;


import io.moquette.broker.Server;

public class ServerInstance {
    private static final Object INSTANCE_LOCK = new Object();
    private static Server serverInstance = null;

    private ServerInstance() {
    }

    public static Server getServerInstance() {
        try {
            if (serverInstance == null) {
                synchronized (INSTANCE_LOCK) {
                    if (serverInstance == null) {
                        serverInstance = new Server();
                        Server server2 = serverInstance;
                        return server2;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serverInstance;
    }
}
