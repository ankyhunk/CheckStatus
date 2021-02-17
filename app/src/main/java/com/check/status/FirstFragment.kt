package com.check.status

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.annotation.RequiresPermission
import androidx.fragment.app.Fragment
import com.check.status.BroadCastReceivers.CheckStatusBroadcastReceiver
import com.check.status.Services.NetworkStatusCheckService

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

private const val TAG = "FirstFragment"

class FirstFragment : Fragment(), BroadCastReceiverCallbacks {

    private lateinit var wifiStatusBroadcastReceiver: CheckStatusBroadcastReceiver
    private val WIFI_REQUEST_CODE = 100
    private val BLUETOOTH_REQUEST_CODE = 101

    @RequiresPermission(allOf = ["android.permission.BLUETOOTH", "android.permission.BLUETOOTH_ADMIN"])
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_first, container, false)

        // UI Components
        val wifiSwitch = view.findViewById<Switch>(R.id.switch_network_status)
        val bluetoothSwitch = view.findViewById<Switch>(R.id.switch_bluetooth_status)
        val serviceSwitch = view.findViewById<Switch>(R.id.switch_service)

        //Create Notification channel
        createNotificationChannel(context)

        // Set the listener to the Service switch to set Service ON & OFF
        serviceSwitch.apply {
            // Initialize the service
            val service = Intent(context, NetworkStatusCheckService::class.java)
            //Check if service is running or not and then set the switch checked to true or false
            if (context?.startService(service) != null)
                isChecked = true
            else {
                isChecked = false
                context?.stopService(service)
            }
            //Call the service based on the Service Switch status
            setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    val service = Intent(context, NetworkStatusCheckService::class.java)
                    context?.startService(service)
                } else {
                    val service = Intent(context, NetworkStatusCheckService::class.java)
                    // Stop the service and dismiss the notifications
                    context?.stopService(service)
                    dismissNotifications(context)
                }
            }
        }

        // Wifi Switch
        wifiSwitch.apply {
            //Set the Wifi Switch state
            isChecked = checkWiFiStatus(context)
            setOnClickListener {
                setWiFiStatus(this.isChecked, WIFI_REQUEST_CODE)
            }
        }

        // Bluetooth Switch
        bluetoothSwitch.apply {
            isChecked = checkBluetoothStatus(context)
            setOnClickListener {
                setBluetoothStatus(this.isChecked, BLUETOOTH_REQUEST_CODE)
            }
        }

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the BroadcastReceiver
        wifiStatusBroadcastReceiver = CheckStatusBroadcastReceiver()
        wifiStatusBroadcastReceiver.registerBroadCastReceiverCallbacks(this)

        // Initialize the intentFilter
        val intentFilter = IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION)
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        // Register the BroadcastReceiver
        context?.registerReceiver(wifiStatusBroadcastReceiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        // UnRegister the BroadcastReceiver
        context?.unregisterReceiver(wifiStatusBroadcastReceiver)
    }

    @RequiresPermission("android.permission.BLUETOOTH")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            WIFI_REQUEST_CODE -> {
                //Check for the Wifi Status
                view?.findViewById<Switch>(R.id.switch_network_status)?.isChecked =
                    checkWiFiStatus(context)
            }

            BLUETOOTH_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    Log.d(TAG, "RESULT_OK")
                    updateBluetoothSwitch()
                }
            }
        }
    }

    override fun wifiStatusChange(message: String) {
        //Check for the Wifi Status
        view?.findViewById<Switch>(R.id.switch_network_status)?.isChecked =
            checkWiFiStatus(context)
    }


    @RequiresPermission("android.permission.BLUETOOTH")
    override fun bluetoothStatusChange(message: String) {
        updateBluetoothSwitch()
    }

    @RequiresPermission("android.permission.BLUETOOTH")
    private fun updateBluetoothSwitch() {
        //Check for the Bluetooth Status
        view?.findViewById<Switch>(R.id.switch_bluetooth_status)?.isChecked =
            checkBluetoothStatus(context)
    }
}