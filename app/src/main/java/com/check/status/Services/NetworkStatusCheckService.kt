package com.check.status.Services

import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.IBinder
import android.util.Log
import com.check.status.BroadCastReceiverCallbacks
import com.check.status.BroadCastReceivers.CheckStatusBroadcastReceiver
import com.check.status.sendNotification

private const val TAG = "WifiStatusCheckService"

class NetworkStatusCheckService : Service(), BroadCastReceiverCallbacks {

    private lateinit var wifiStatusBroadcastReceiver: CheckStatusBroadcastReceiver


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand")
        return START_STICKY

    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
        // Initialize the BroadcastReceiver
        wifiStatusBroadcastReceiver = CheckStatusBroadcastReceiver()
        wifiStatusBroadcastReceiver.registerBroadCastReceiverCallbacks(this)

        // Initialize the intentFilter
        val intentFilter = IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION)
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        registerReceiver(wifiStatusBroadcastReceiver, intentFilter)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
        // UnRegister the BroadcastReceiver
        unregisterReceiver(wifiStatusBroadcastReceiver)
    }

    override fun wifiStatusChange(message: String) {
        sendNotification(message, this)
    }

    override fun bluetoothStatusChange(message: String) {
        sendNotification(message, this)
    }

}