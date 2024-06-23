package com.project.mqtt.mqtt;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.project.mqtt.MainActivity;
import com.project.mqtt.R;

import java.io.File;
import java.util.HashMap;
import java.util.Properties;

import io.netty.handler.codec.rtsp.RtspHeaders;

public class MqttAppService extends Service {
    public boolean isServerrunning = false;
    private final IBinder mBinder = new LocalBinder();
    Properties props = new Properties();

    public void onCreate() {
        super.onCreate();
        startForeground(1337, new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_launcher_foreground).setContentTitle("MQttServer App").setContentText("Server is Running...").setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_IMMUTABLE)).build());
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        /*File configFile = (File) intent.getSerializableExtra("configFile");
        HashMap map = (HashMap) intent.getSerializableExtra("props");
        if (!(map == null || map.get("host") == null || map.get(RtspHeaders.Values.PORT) == null)) {
            this.props.put("host", map.get("host"));
            this.props.put(RtspHeaders.Values.PORT, map.get(RtspHeaders.Values.PORT));
        }*/
        this.isServerrunning = StartServer.getServerStatus();
        if (this.isServerrunning) {
            return Service.START_NOT_STICKY;
        }
        new Thread(new StartServer(/*configFile*/)).start();
        this.isServerrunning = StartServer.getServerStatus();
        return Service.START_NOT_STICKY;
    }

    public boolean getServerStatus() {
        return StartServer.getServerStatus();
    }

    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    public class LocalBinder extends Binder {
        public LocalBinder() {
        }

        public MqttAppService getService() {
            return MqttAppService.this;
        }
    }

    public void onDestroy() {
        super.onDestroy();
        StopServer.stopServer();
        StartServer.setServerStatus(false);
        this.isServerrunning = false;
    }

    public String getBrokerURL() {
        return this.props.get("host").toString() + ":" + this.props.get(RtspHeaders.Values.PORT);
    }
}
