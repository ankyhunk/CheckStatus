package com.check.status.BroadCastReceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.annotation.RequiresPermission
import com.check.status.BroadCastReceiverCallbacks
import com.check.status.checkBluetoothStatus
import com.check.status.checkWiFiStatus

private const val TAG = "BroadcastReceiver"

class CheckStatusBroadcastReceiver : BroadcastReceiver() {

    private var callback: BroadCastReceiverCallbacks? = null

    @RequiresPermission("android.permission.BLUETOOTH")
    override fun onReceive(context: Context?, intent: Intent?) {

        Log.d(TAG, "onReceive")
        if (callback == null)
            Log.d(TAG, "Callback is NULL")
        else
            Log.d(TAG, "Callback is not NULL")

        val wifiStatus = when (checkWiFiStatus(context)) {
            true -> "WiFi: ON"
            else -> "WiFi: OFF"
        }
        val bluetoothStatus = when (checkBluetoothStatus(context)) {
            true -> "Bluetooth: ON"
            else -> "Bluetooth: OFF"
        }

        StringBuilder().apply {
            append(wifiStatus)
            append("\n")
            append("       ")
            append(bluetoothStatus)
            toString().also { message ->
                Log.d(TAG, message)
                when (intent!!.action) {
                    "android.net.wifi.WIFI_STATE_CHANGED" -> {
                        if (callback != null)
                            callback!!.wifiStatusChange(message)

                    }
                    "android.bluetooth.adapter.action.STATE_CHANGED" -> {
                        if (callback != null)
                            callback!!.bluetoothStatusChange(message)
                    }
                }
            }
        }
    }

    fun registerBroadCastReceiverCallbacks(callbacks: BroadCastReceiverCallbacks) {
        this.callback = callbacks
    }

}