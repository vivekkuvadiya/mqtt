package com.project.mqtt.mqtt;

public class StopServer {
    public static boolean stopServer() {
        try {
            ServerInstance.getServerInstance().stopServer();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
