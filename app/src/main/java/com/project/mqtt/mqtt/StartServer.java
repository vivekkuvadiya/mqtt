package com.project.mqtt.mqtt;

import java.io.File;

public class StartServer implements Runnable {
    private static boolean serverStatus = false;
    File file;

    public StartServer(File file2) {
        this.file = file2;
    }

    public StartServer() {
        this.file = new File("");
    }

    public static boolean getServerStatus() {
        return serverStatus;
    }

    public static void setServerStatus(boolean serverStatus2) {
        serverStatus = serverStatus2;
    }

    public void run() {
        serverStatus = serverStart();
    }

    private boolean serverStart() {
        try {
            ServerInstance.getServerInstance().startServer(/*this.file*/);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
