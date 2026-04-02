package com.example.altitude.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class CompassTracker(context: Context) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

    private var onAzimuthReceived: ((Float) -> Unit)? = null

    private val rotationMatrix = FloatArray(9)
    private val orientationValues = FloatArray(3)

    fun startTracking(callback: (Float) -> Unit) {
        onAzimuthReceived = callback
        rotationSensor?.let {
            // Changed from SENSOR_DELAY_UI to SENSOR_DELAY_GAME for high-refresh-rate screens (60-120Hz)
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
            SensorManager.getOrientation(rotationMatrix, orientationValues)

            var azimuth = Math.toDegrees(orientationValues[0].toDouble()).toFloat()
            if (azimuth < 0) {
                azimuth += 360f
            }
            onAzimuthReceived?.invoke(azimuth)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}