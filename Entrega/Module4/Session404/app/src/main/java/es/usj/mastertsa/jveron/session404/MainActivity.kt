package es.usj.mastertsa.jveron.session404

import android.Manifest
import android.R
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import es.usj.mastertsa.jveron.session404.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val bindings: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var devices: Set<BluetoothDevice>

    private val visibilityRequest =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { }

    private val permissions =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindings.root)

        val bluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        bindings.btnList.setOnClickListener { list() }
        bindings.btnOn.setOnClickListener { on() }
        bindings.btnOff.setOnClickListener { off() }
        bindings.btnVisible.setOnClickListener { visible() }
    }

    private fun list() {
        devices = bluetoothAdapter.bondedDevices ?: setOf()
        val list = ArrayList<String>()
        for (bt in devices)
            list.add(bt.name)
        Toast.makeText(applicationContext, "Showing Paired Devices",
            Toast.LENGTH_SHORT).show()
        val adapter = ArrayAdapter(this, R.layout.simple_list_item_1, list)
        bindings.lv.adapter = adapter
    }

    private fun on() {
        if (!isBluetoothPermissionGranted()) {
            permissions.launch(Manifest.permission.BLUETOOTH +
                    Manifest.permission.BLUETOOTH_ADMIN)
        }
    }

    private fun off() {
        bluetoothAdapter.disable()
        Toast.makeText(applicationContext, "Bluetooth disabled",
            Toast.LENGTH_LONG).show()
    }
    private fun visible() {
        val visibleIntent =
            Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        visibilityRequest.launch(visibleIntent)
    }

    private fun isBluetoothPermissionGranted(): Boolean {
        var granted = false
        val bluetoothGranted =
            checkSelfPermission(Manifest.permission.BLUETOOTH)
        val bluetoothAdminGranted =
            checkSelfPermission(Manifest.permission.BLUETOOTH_ADMIN)
        if (bluetoothGranted == PackageManager.PERMISSION_GRANTED &&
            bluetoothAdminGranted == PackageManager.PERMISSION_GRANTED) {
            granted = true
        }
        return granted
    }
}