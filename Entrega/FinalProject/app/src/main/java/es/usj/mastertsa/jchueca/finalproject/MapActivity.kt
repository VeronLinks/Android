package es.usj.mastertsa.jchueca.finalproject

import android.Manifest
import android.R.attr
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.DirectionsApi
import com.google.maps.DirectionsApiRequest
import com.google.maps.GeoApiContext
import com.google.maps.model.*
import es.usj.mastertsa.jchueca.finalproject.databinding.ActivityMapBinding
import java.io.IOException
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.*
import kotlin.random.Random
import android.R.attr.path
import android.content.Intent
import android.graphics.Color
import android.net.Uri

import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.LatLng as LatLng1


class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private var longitude: String? = null
    private var latitude: String? = null
    private var extra: Bundle? = null
    private var mapFragment: SupportMapFragment? = null
    private lateinit var map: GoogleMap
    private var address: Address? = null
    private lateinit var bindings: ActivityMapBinding

    private lateinit var userLocation: LatLng1
    private lateinit var targetLocation: LatLng1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindings = ActivityMapBinding.inflate(layoutInflater)
        setContentView(bindings.root)
        bindings.btnBack.setOnClickListener { finish() }

        extra = intent.extras
        var dLongitude = extra?.getDouble("longitude")
        var dLatitude = extra?.getDouble("latitude")

        // dLongitude = if(!longitude.isNullOrEmpty()) longitude?.toDouble() else -0.89135598
        // dLatitude = if(!latitude.isNullOrEmpty()) latitude?.toDouble() else 41.64511038

        userLocation = LatLng1(dLatitude!!, dLongitude!!)
        targetLocation = getRandomLocation(userLocation, 1000)

        mapAvailable()

        val gmmIntentUri = Uri.parse("http://maps.google.com/maps?" +
                "saddr=${userLocation.latitude},${userLocation.longitude}&" +
                "daddr=${targetLocation.latitude},${targetLocation.longitude}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        bindings.btnGoToMaps.setOnClickListener { startActivity(mapIntent) }

    }

    private fun mapAvailable() {
        if (mapFragment == null) {
            mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            mapFragment!!.getMapAsync(this@MapActivity)
        }
        if (mapFragment != null) {
            Toast.makeText(this, "MapActivity de Google available", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        try {
            val geoCoder = Geocoder(this, Locale.getDefault())
            val addresses = geoCoder.getFromLocation(userLocation.latitude, userLocation.longitude, 5)
            map = googleMap
            map.mapType = GoogleMap.MAP_TYPE_NORMAL


            if     (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)   != PackageManager.PERMISSION_GRANTED
                 && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                // TODO: Request permissions in the previous activity.
                    // Remember to disable the permissions in the mobile to check if this works
                    // https://stackoverflow.com/questions/40142331/how-to-request-location-permission-at-runtime
                Toast.makeText(this, "Please allow location permissions", Toast.LENGTH_SHORT).show()
                finish()
                return
            }

            map.isMyLocationEnabled = true
            map.moveCamera( CameraUpdateFactory.newLatLngZoom(userLocation, 20f))

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
                    .position(userLocation))

            map.addMarker(
                MarkerOptions()
                    .title(addressCoordinates)
                    .position(targetLocation))

            //Draw the line
            val path: MutableList<LatLng1> = ArrayList()
            path.add(userLocation)
            path.add(targetLocation)
            if (path.isNotEmpty()) {
                val opts = PolylineOptions().addAll(path).color(Color.BLUE).width(5f)
                map.addPolyline(opts)
            }

            val cameraPosition = CameraPosition.builder()
                .target(userLocation)
                .zoom(16.0f)
                .tilt(45.0f)
                .bearing(45.0f)
                .build()

            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),2000,null)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun getDirection(origin: LatLng1, destination: LatLng1): List<LatLng1>{

        val path: MutableList<LatLng1> = ArrayList()

        //Execute Directions API request
        val apiKey = resources.getString( R.string.google_maps_key)
        val context: GeoApiContext = GeoApiContext.Builder()
            .apiKey(apiKey)
            .build()
        val req: DirectionsApiRequest =
            DirectionsApi.getDirections(context, origin.toString(), destination.toString())
        try {
            val res: DirectionsResult = req.await()

            //Loop through legs and steps to get encoded polylines of each step
            if (res.routes != null && res.routes.isNotEmpty()) {
                val route: DirectionsRoute = res.routes[0]
                if (route.legs != null) {
                    for (i in route.legs.indices) {
                        val leg: DirectionsLeg = route.legs[i]
                        if (leg.steps != null) {
                            for (j in leg.steps.indices) {
                                val step: DirectionsStep = leg.steps[j]
                                if (step.steps != null && step.steps.isNotEmpty()) {
                                    for (k in step.steps.indices) {
                                        val step1: DirectionsStep = step.steps[k]
                                        val points1: EncodedPolyline = step1.polyline
                                        //Decode polyline and add points to list of route coordinates
                                        val coords1: MutableList<com.google.maps.model.LatLng>? = points1.decodePath()
                                        if (coords1 != null) {
                                            for (coord1 in coords1) {
                                                path.add(LatLng1(coord1.lat, coord1.lng))
                                            }
                                        }
                                    }
                                } else {
                                    val points: EncodedPolyline = step.polyline
                                    //Decode polyline and add points to list of route coordinates
                                    val coords: MutableList<com.google.maps.model.LatLng>? = points.decodePath()
                                    if (coords != null) {
                                        for (coord in coords) {
                                            path.add(LatLng1(coord.lat, coord.lng))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {

            path.add(origin)
            path.add(destination)
            //path.add(LatLng1(origin.latitude, origin.longitude))
            //path.add(LatLng1(destination.latitude, destination.longitude))
            e.printStackTrace()
        }
        return path;
    }

    private fun getRandomLocation(point: LatLng1, radius: Int): LatLng1 {

        val x0: Double = point.latitude
        val y0: Double = point.longitude

        // Convert radius from meters to degrees
        val radiusInDegrees = (radius / 111000.0)
        val v: Double = Random.nextDouble()
        val t = 2 * Math.PI * v
        val x = radiusInDegrees * cos(t)
        val y = radiusInDegrees * sin(t)

        // Adjust the x-coordinate for the shrinking of the east-west distances
        val newX = x / cos(y0)
        val foundLatitude = newX + x0
        val foundLongitude = y + y0

        return LatLng1(foundLatitude, foundLongitude)
    }
}