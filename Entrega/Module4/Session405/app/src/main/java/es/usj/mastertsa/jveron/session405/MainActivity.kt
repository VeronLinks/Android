package es.usj.mastertsa.jveron.session405

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.core.app.ActivityCompat
import es.usj.mastertsa.jveron.session405.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
    }

    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    private val bindings: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(bindings.root)
        getGpsLocation()
        bindings.btnGo.setOnClickListener {
            try {
                val intent = Intent(this@MainActivity,
                    MapActivity::class.java)
                intent.putExtra(LATITUDE,
                    bindings.tvLongitude.text.toString())
                intent.putExtra(LONGITUDE, bindings.tvLatitude.text.toString())
                startActivity(intent)
            } catch (ex: Exception) {
                Toast.makeText(this@MainActivity, ex.message,
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getGpsLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as
                LocationManager
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            return
        }
        val loc =
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        showLocation(loc)
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                showLocation(location)
            }
            override fun onStatusChanged(provider: String, status:
            Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER, 5000, 0f, locationListener)
    }

    private fun showLocation(loc: Location?) = if (loc != null) {
            bindings.tvLatitude.setText(loc.latitude.toString())
            bindings.tvLongitude.setText(loc.longitude.toString())
            mutableListOf(loc.latitude.toString(), loc.longitude.toString())
        }
        else {
            bindings.tvLatitude.setText(40.4167754.toString())
            bindings.tvLongitude.setText((-3.7037901999999576).toString())
            mutableListOf(40.4167754.toString(), (-3.7037901999999576).toString(), "Default location")
        }
}