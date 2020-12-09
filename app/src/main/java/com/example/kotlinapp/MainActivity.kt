package com.example.kotlinapp

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    var searchbutton:Button?=null
    var listView:ListView?=null
    var bluetoothAdapter:BluetoothAdapter?=null
    var devices= mutableListOf<String>()
    var addresses= mutableListOf<String>()
    var arrayAdapter:ArrayAdapter<String>?=null

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.v("Onreceive","jklkjl")
            var action: String? = intent.action
                when (action) {
                    BluetoothDevice.ACTION_FOUND -> {
                        // Discovery has found a device. Get the BluetoothDevice
                        // object and its info from the Intent.
                        val device: BluetoothDevice? =
                            intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                        val deviceName = device?.name
                        val deviceHardwareAddress = device?.address // MAC address
                        val rssi: String? = Integer.toString(
                            intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE)
                                .toInt()
                        )
                        Log.v(
                            "Device :",
                            " Name: $deviceName Address: $deviceHardwareAddress RSSI: $rssi"
                        )

                    }
                }
            }
        }
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var textView=findViewById<TextView>(R.id.textView)
        listView=findViewById<ListView>(R.id.listview)
        searchbutton=findViewById<Button>(R.id.button)
        
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            textView.setText("Device does not support bluetooth")
        }
        else{
            if (!bluetoothAdapter?.isEnabled()) { //if bluetooth not enabled
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, 0)
            } else {
                Toast.makeText(applicationContext, "Already on", Toast.LENGTH_LONG).show()
                //as bluetooth is on do the operation
                arrayAdapter=ArrayAdapter(this,android.R.layout.simple_list_item_1,devices)
                listView?.setAdapter(arrayAdapter)

                var intentFilter: IntentFilter =IntentFilter()
                intentFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
                intentFilter.addAction(BluetoothDevice.ACTION_FOUND)
                intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
                intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
                registerReceiver(broadcastReceiver,intentFilter)
                bluetoothAdapter?.startDiscovery()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK){
            Toast.makeText(getApplicationContext(),"Turned on",Toast.LENGTH_LONG).show()
            //as bluetooth is on do the operation
            arrayAdapter=ArrayAdapter(this,android.R.layout.simple_list_item_1,devices)
            listView?.setAdapter(arrayAdapter)

            var intentFilter: IntentFilter =IntentFilter()
            intentFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
            intentFilter.addAction(BluetoothDevice.ACTION_FOUND)
            intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
            intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
            registerReceiver(broadcastReceiver,intentFilter)
            bluetoothAdapter?.startDiscovery()
        }
        if(resultCode == RESULT_CANCELED){
            Toast.makeText(getApplicationContext(),"Bluetooth failed to turn on",Toast.LENGTH_LONG).show();
        }
    }

    fun search(view: View) {
        textView.setText("Searching...")
        searchbutton?.setEnabled(false)
        bluetoothAdapter?.startDiscovery()
        Log.v("opp","yiuiuoiuoiu")
    }
}
