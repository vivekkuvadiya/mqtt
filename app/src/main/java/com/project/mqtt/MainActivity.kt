package com.project.mqtt

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.IBinder
import android.provider.Settings
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.project.mqtt.databinding.ActivityMainBinding
import com.project.mqtt.mqtt.MqttAppService
import com.project.mqtt.mqtt.ServerInstance
import io.moquette.BrokerConstants
import io.moquette.broker.config.MemoryConfig
import java.io.File
import java.util.Properties


class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        askforPermission()
        val intent = Intent()
        intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
        val uri = Uri.fromParts("package", packageName, null)
        intent.setData(uri)
        startActivity(intent)
        binding.btnStart.setOnClickListener{
            /*val intent = Intent(this,MqttAppService::class.java)
            startService(intent)
            bindService(intent,mConnection,64)*/




            val memoryConfig = MemoryConfig(Properties())
            memoryConfig.setProperty(
                BrokerConstants.PERSISTENT_STORE_PROPERTY_NAME,
                Environment.getExternalStorageDirectory().absolutePath + File.separator + BrokerConstants.DEFAULT_MOQUETTE_STORE_H2_DB_FILENAME
            )

            ServerInstance.getServerInstance().startServer(memoryConfig)
        }

        binding.btnStop.setOnClickListener {

            stopService(Intent(this,MqttAppService::class.java))
        }

    }
    private val mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
        }
    }

    private fun askforPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == 0
        ) {
            return false
        }
        ActivityCompat.requestPermissions(
            this,
            arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            2
        )
        return true
    }

    /*private fun createConfigurationFile(): File {
        val confFile: File =
            File(getDir("media", 0).getAbsolutePath() + Utils.BROKER_CONF_FILE)
        try {
            if (confFile.exists()) {
                return writeToConfFile(confFile)
            }
            if (confFile.createNewFile()) {
                return writeToConfFile(confFile)
            }
            return confFile
        } catch (e: Exception) {
            e.printStackTrace()
            return confFile
        }
    }*/

    /*@Throws(java.lang.Exception::class)
    private fun writeToConfFile(confFile: File): File {
        val fileOutputStream = FileOutputStream(confFile)
        fileOutputStream.write(
            ("port " + (this.brokerPort.getText().toString() + "\n").toString()).toByteArray()
        )
        this.props.put(
            RtspHeaders.Values.PORT,
            this.brokerPort.getText().toString().toString()
        )
        fileOutputStream.write((("host " + Util.getBrokerURL(getContext())).toString() + "\n").getBytes())
        this.props.put("host", Util.getBrokerURL(getContext()))
        fileOutputStream.write(("websocket_port " + Constants.WEBSOCKET_PORT.toString() + "\n").toByteArray())
        fileOutputStream.close()
        return confFile
    }*/
}