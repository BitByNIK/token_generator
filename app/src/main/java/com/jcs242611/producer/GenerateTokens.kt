package com.jcs242611.producer

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class GenerateTokens : Service() {
    private var count = 0
    private lateinit var database: ProducerDatabase
    private val handler = Handler(Looper.getMainLooper())

    private val runnable = object : Runnable {
        override fun run() {
            Thread {
                val token = newToken()
                saveToken(token)
                count++
                if (count % 4 == 0) {
                    val newTokensIntent = Intent("com.jcs242611.producer.TOKENS_READY")
                    newTokensIntent.setPackage("com.jcs242611.consumer")
                    sendBroadcast(newTokensIntent)
                }
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(this@GenerateTokens, "Token: ${token.timestamp}, ${token.latitude}, ${token.longitude}", Toast.LENGTH_LONG).show()
                }
            }.start()

            handler.postDelayed(this, 15000)
        }
    }

    override fun onCreate() {
        super.onCreate()
        database = ProducerDatabase.getDatabase(applicationContext)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handler.post(runnable)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    private fun newToken(): Token {
        val (latitude, longitude) = getLocation()
        return Token(timestamp = getCurrentTimestamp(), latitude = latitude, longitude = longitude)
    }

    private fun getCurrentTimestamp(): String {
        return SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss", Locale.getDefault()).format(Date())
    }

    private fun getLocation(): Pair<Double, Double> {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return Pair(0.0, 0.0)
        }

        var result: Pair<Double, Double>? = null
        val latch = CountDownLatch(1)
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@GenerateTokens)

        // Request the current location asynchronously.
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    result = Pair(location.latitude, location.longitude)
                }
                latch.countDown()
            }
            .addOnFailureListener {
                latch.countDown()
            }

        // Wait up to 1 second for the location result.
        latch.await(1, TimeUnit.SECONDS)

        // If no location was obtained or result is (0.0, 0.0), fall back to last known location.
        if (result == null || (result!!.first == 0.0 && result!!.second == 0.0)) {
            result = getFallbackLocation()
        }
        return result ?: Pair(0.0, 0.0)
    }

    private fun getFallbackLocation(): Pair<Double, Double> {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return Pair(0.0, 0.0)
        }

        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val location: Location? = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

        return location?.let { Pair(it.latitude, it.longitude) } ?: Pair(0.0, 0.0)
    }

    private fun saveToken(token: Token) {
        CoroutineScope(Dispatchers.IO).launch {
            database.tokenDao().insertToken(token)
        }
    }
}