package es.usj.mastertsa.jveron.session405

import android.Manifest
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import es.usj.mastertsa.jveron.session405.databinding.ActivityMapBinding
import java.io.IOException
import java.util.*

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private val bindings: ActivityMapBinding by lazy {
        ActivityMapBinding.inflate(layoutInflater)
    }

    private var longitude: String? = null
    private var latitude: String? = null
    private var extra: Bundle? = null
    private var mapFragment: SupportMapFragment? = null
    private lateinit var map: GoogleMap
    private var address: Address? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(bindings.root)
        extra = intent.extras
        longitude = extra!!.getString("Longitud")
        latitude = extra!!.getString("Latitud")
        loadMap()
    }

    private fun loadMap() {
        if (mapFragment == null) {
            mapFragment =
                supportFragmentManager.findFragmentById(R.id.map) as
                        SupportMapFragment?
            mapFragment!!.getMapAsync(this@MapActivity)
        }
        if (mapFragment != null) {
            Toast.makeText(this, "MapActivity by Google available",
                Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val dLatitude = latitude?.toDouble() ?: 0.0
        val dLongitude = longitude?.toDouble() ?: 0.0
        try {
            val geoCoder = Geocoder(this, Locale.getDefault())
            val addresses = geoCoder.getFromLocation(dLatitude,
                dLongitude, 5)
            map = googleMap
            map.mapType = GoogleMap.MAP_TYPE_NORMAL
            val newLocation = LatLng(dLatitude, dLongitude)
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
                return
            }
            map.isMyLocationEnabled = true
            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(newLocation,
                    20f))
            var addressCoordinates = "Sin Datos"
            if (addresses.size > 0) {
                address = addresses[0]
                addressCoordinates = (address!!.getAddressLine(0)
                        + " " + address!!.postalCode
                        + " " + address!!.locality
                        + ", " + address!!.countryName)
            }
            map.addMarker(
                MarkerOptions()
                    .title(addressCoordinates)
                    .position(newLocation))
            val cameraPosition = CameraPosition.builder()
                .target(newLocation)
                .zoom(16.0f)
                .tilt(45.0f)
                .bearing(45.0f)
                .build()

            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                2000, null)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}