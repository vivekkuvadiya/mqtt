package com.project.mqtt.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttCallbackHandler implements MqttCallbackExtended {
    public void connectComplete(boolean reconnect, String serverURI) {
        if (reconnect) {

        }
    }

    public void connectionLost(Throwable throwable) {
    }

    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    }
}
