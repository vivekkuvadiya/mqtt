package com.project.mqtt

import android.content.Context
import android.net.wifi.WifiManager
import android.text.format.Formatter

object Utils {

    const val BROKER_CONF_FILE = "moqette.conf"

    fun getBrokerURL(ctx: Context): String {
        return Formatter.formatIpAddress((ctx.getSystemService(Context.WIFI_SERVICE) as WifiManager).connectionInfo.ipAddress)
    }

}