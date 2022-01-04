package es.usj.mastertsa.jchueca.finalproject

import android.Manifest
import android.annotation.SuppressLint
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
import com.google.maps.model.*
import es.usj.mastertsa.jchueca.finalproject.databinding.ActivityMapBinding
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.*
import kotlin.random.Random
import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Location
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.LatLng as LatLng1
import com.google.android.material.snackbar.Snackbar
import es.usj.mastertsa.jchueca.finalproject.notifications.GlobalNotificationBuilder
import es.usj.mastertsa.jchueca.finalproject.notifications.NotificationDatabase
import es.usj.mastertsa.jchueca.finalproject.notifications.NotificationUtils

private const val RADIUS = 50
private const val MIN_DISTANCE_TO_COMPLETE = 20

class MapActivity : AppCompatActivity(), OnMapReadyCallback {


    private var fusedLocationClient: FusedLocationProviderClient? = null

    private var challengeId: Int = 0
    private var extra: Bundle? = null
    private var mapFragment: SupportMapFragment? = null
    private lateinit var map: GoogleMap
    private var address: Address? = null
    private lateinit var bindings: ActivityMapBinding

    private lateinit var userLocation: LatLng1
    private lateinit var targetLocation: LatLng1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        bindings = ActivityMapBinding.inflate(layoutInflater)
        setContentView(bindings.root)
        bindings.btnBack.setOnClickListener { finish() }

        enableNotifications()

        extra = intent.extras
        challengeId = extra?.getInt("challengeId")!!
        val dLongitude = extra?.getDouble("longitude")
        val dLatitude = extra?.getDouble("latitude")
        targetLocation = LatLng1(dLatitude!!, dLongitude!!)

        mapAvailable()

        bindings.btnGoToMaps.isEnabled = false
        supportActionBar!!.hide()
    }

    private fun setGoToMapsButton() {
        val gmmIntentUri = Uri.parse(
            "http://maps.google.com/maps?" +
                    "saddr=${userLocation.latitude},${userLocation.longitude}&" +
                    "daddr=${targetLocation.latitude},${targetLocation.longitude}"
        )
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        bindings.btnGoToMaps.setOnClickListener { startActivity(mapIntent) }
        bindings.btnGoToMaps.isEnabled = true
    }

    private fun mapAvailable() {
        if (mapFragment == null) {
            mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            mapFragment!!.getMapAsync(this@MapActivity)
        }
        //if (mapFragment != null) {
        //    Toast.makeText(this, "MapActivity de Google available", Toast.LENGTH_SHORT).show()
        //}
    }


    override fun onMapReady(googleMap: GoogleMap) {

        if     (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)   != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                PackageManager.PERMISSION_GRANTED)
            Toast.makeText(this, "Please allow location permissions", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        try {
            val geoCoder = Geocoder(this, Locale.getDefault())
            map = googleMap
            map.mapType = GoogleMap.MAP_TYPE_NORMAL

            fusedLocationClient!!.lastLocation
                .addOnSuccessListener(this) { location ->
                    // Got last known location. In some rare situations this can be null.
                    userLocation = LatLng1(location.latitude, location.longitude)
                    val challenge = SaveLoad.loadChallenge(this, challengeId)!!

                    if(targetLocation.latitude==0.0&&targetLocation.longitude==0.0){
                        targetLocation = getRandomLocation(userLocation)
                        challenge.latitude = targetLocation.latitude
                        challenge.longitude = targetLocation.longitude
                        val newAddress = geoCoder.getFromLocation(targetLocation.latitude, targetLocation.longitude, 1)
                        challenge.description = "Go to ${newAddress[0].getAddressLine(0)} as fast as possible!"

                    }
                    val results = FloatArray(1)
                    Location.distanceBetween(userLocation.latitude, userLocation.longitude,
                        targetLocation.latitude, targetLocation.longitude,
                        results)
                    if(results[0] < MIN_DISTANCE_TO_COMPLETE){
                        challenge.isCompleted = true

                        showNotification()

                        finish()
                    }
                    SaveLoad.saveChallenge(this, challenge)
                    setGoToMapsButton()
                    setMapOnceUserLocationExists(geoCoder)
                }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }


    private fun setMapOnceUserLocationExists(geoCoder: Geocoder) {
        val addresses = geoCoder.getFromLocation(userLocation.latitude, userLocation.longitude, 5)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        map.isMyLocationEnabled = true
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 20f))

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
                .position(userLocation)
        )

        map.addMarker(
            MarkerOptions()
                .title(addressCoordinates)
                .position(targetLocation)
        )

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

        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null)
    }

    private fun getRandomLocation(point: LatLng1): LatLng1 {

        val x0: Double = point.latitude
        val y0: Double = point.longitude

        // Convert radius from meters to degrees
        val radiusInDegrees = (RADIUS / 111000.0)
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


    // NOTIFICATIONS
    private fun showNotification(isStarting: Boolean = true) {
        enableNotifications()
        generateNotification(isStarting)
    }

    private val notificationManager : NotificationManagerCompat by lazy {
        NotificationManagerCompat.from(applicationContext)
    }

    private fun enableNotifications() {
        val enabled = notificationManager.areNotificationsEnabled()
        if (!enabled){
            Snackbar.make(
                findViewById(R.id.mainLayout),
                "Enable notifications",
                Snackbar.LENGTH_LONG
            ).setAction("ENABLE"){
                openNotificationsSettingsForApp()
            }.show()
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun generateNotification(starting: Boolean) {
        val notification = if (starting) NotificationDatabase.enteringNotification
        else NotificationDatabase.existingNotification(applicationContext)
        val channelId = NotificationUtils.createNotificationChannel(this, notification)
        val notificationStyle = NotificationCompat.BigTextStyle()
            .bigText(notification.text)
            .setBigContentTitle(notification.title)
            .setSummaryText(notification.summary)

        val notifyIntent = Intent(this, ChallengesActivity::class.java)
        notifyIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val notifyPendingIntent = PendingIntent.getActivity(
            this,
            0,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notificationCompatBuilder: NotificationCompat.Builder = NotificationCompat.Builder(
            applicationContext, channelId!!
        )
        GlobalNotificationBuilder.notificationCompatBuilderInstance = notificationCompatBuilder

        val playingNotification: Notification = notificationCompatBuilder
            .setStyle(notificationStyle)
            .setContentTitle(notification.contentTitle)
            .setContentText(notification.contentText)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    resources,
                    R.mipmap.ic_launcher
                )
            )
            .setContentIntent(notifyPendingIntent)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setColor(ContextCompat.getColor(applicationContext, R.color.purple_500))
            .setCategory(Notification.CATEGORY_REMINDER)
            .setPriority(notification.priority)
            .setVisibility(notification.channelLockscreenVisibility)
            .build()
        notificationManager.notify(
            current_id,
            playingNotification
        )
    }

    companion object {
        var current_id = 0
    }

    private fun openNotificationsSettingsForApp() {
        val intent = Intent()
        intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
        intent.putExtra("app_package", packageName)
        intent.putExtra("app_uid", applicationInfo.uid)
        startActivity(intent)
    }

}