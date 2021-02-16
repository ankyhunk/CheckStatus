package com.check.status.Services

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.IBinder
import android.util.Log
import com.check.status.BroadCastReceivers.CheckStatusBroadcastReceiver

private const val TAG = "WifiStatusCheckService"

class WifiStatusCheckService : Service() {

    private lateinit var wifiStatusBroadcastReceiver: CheckStatusBroadcastReceiver

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand")
        return START_STICKY

    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
        wifiStatusBroadcastReceiver = CheckStatusBroadcastReceiver()
        val intentFilter = IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION)
        registerReceiver(wifiStatusBroadcastReceiver, intentFilter)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onCreate")
        unregisterReceiver(wifiStatusBroadcastReceiver)
    }

}