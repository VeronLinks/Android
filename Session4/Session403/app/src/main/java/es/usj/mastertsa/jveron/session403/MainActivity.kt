package es.usj.mastertsa.jveron.session403

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import androidx.annotation.RequiresApi
import es.usj.mastertsa.jveron.session403.databinding.ActivityMainBinding
import kotlin.math.abs

class MainActivity : AppCompatActivity(), SensorEventListener {

    private var lastX = 0f
    private var lastY = 0f
    private var lastZ = 0f
    private var deltaXMax = 0f
    private var deltaYMax = 0f
    private var deltaZMax = 0f
    private var deltaX = 0f
    private var deltaY = 0f
    private var deltaZ = 0f
    private var vibrateThreshold = 0f
    lateinit var v: Vibrator
    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null

    private val bindings: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindings.root)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        if (sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            accelerometer = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            sensorManager!!.registerListener(
                this, accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL)
            vibrateThreshold = accelerometer!!.maximumRange / 2
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            v = (getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
        }
        else{
            v = getSystemService(VIBRATOR_SERVICE) as Vibrator
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager?.registerListener(
            this, accelerometer,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }
    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        displayCleanValues()
        displayCurrentValues()
        displayMaxValues()
        deltaX = abs(lastX - event!!.values[0])
        deltaY = abs(lastY - event.values[1])
        deltaZ = abs(lastZ - event.values[2])
        if (deltaX < 2)
            deltaX = 0f
        if (deltaY < 2)
            deltaY = 0f
        if (deltaZ < 2)
            deltaZ = 0f
        lastX = event.values[0]
        lastY = event.values[1]
        lastZ = event.values[2]
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrate()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    private fun displayCleanValues() {
        bindings.currentX.text = "0.0"
        bindings.currentY.text = "0.0"
        bindings.currentZ.text = "0.0"
    }

    private fun displayCurrentValues() {
        bindings.currentX.text = deltaX.toString()
        bindings.currentY.text = deltaY.toString()
        bindings.currentZ.text = deltaZ.toString()
    }

    private fun displayMaxValues() {
        if (deltaX > deltaXMax) {
            deltaXMax = deltaX
            bindings.maxX.text = deltaXMax.toString()
        }
        if (deltaY > deltaYMax) {
            deltaYMax = deltaY
            bindings.maxY.text = deltaYMax.toString()
        }
        if (deltaZ > deltaZMax) {
            deltaZMax = deltaZ
            bindings.maxZ.text = deltaZMax.toString()
        }
    }

    private fun vibrate() {
        if (deltaX > vibrateThreshold ||
            deltaY > vibrateThreshold ||
            deltaZ > vibrateThreshold) {
            v.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK))
        }
    }
}