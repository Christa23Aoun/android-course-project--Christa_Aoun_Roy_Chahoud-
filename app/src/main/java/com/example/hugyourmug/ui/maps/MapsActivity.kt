package com.example.hugyourmug.ui.maps

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.hugyourmug.LoginActivity
import com.example.hugyourmug.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private val LOCATION_PERMISSION_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔐 Session check: if not logged in → go to Login
        val prefs = getSharedPreferences("userData", MODE_PRIVATE)
        if (prefs.getInt("loggedInUserId", -1) == -1) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_maps)

        // 💡 Directions button (bottom card)
        val btnDirections = findViewById<Button>(R.id.btnDirections)
        btnDirections.setOnClickListener {
            val uri = Uri.parse("google.navigation:q=33.8938,35.5018")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setPackage("com.google.android.apps.maps")
            startActivity(intent)
        }

        // Map fragment
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        enableUserLocation()

        // Hug Your Mug location
        val hugYourMug = LatLng(33.8938, 35.5018)

        // custom logo marker (logo.jpg in res/drawable)
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.logo)
        val resizedBitmap: Bitmap = Bitmap.createScaledBitmap(bitmap, 140, 140, false)

        googleMap.addMarker(
            MarkerOptions()
                .position(hugYourMug)
                .title("Hug Your Mug Café")
                .snippet("Open today • 8:00 AM – 10:00 PM")
                .icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap))
        )

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(hugYourMug, 16f))
    }

    private fun enableUserLocation() {
        val fine = Manifest.permission.ACCESS_FINE_LOCATION
        val coarse = Manifest.permission.ACCESS_COARSE_LOCATION

        val fineGranted =
            ContextCompat.checkSelfPermission(this, fine) == PackageManager.PERMISSION_GRANTED
        val coarseGranted =
            ContextCompat.checkSelfPermission(this, coarse) == PackageManager.PERMISSION_GRANTED

        if (fineGranted || coarseGranted) {
            try {
                googleMap.isMyLocationEnabled = true
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(fine, coarse),
                LOCATION_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                try {
                    googleMap.isMyLocationEnabled = true
                } catch (e: SecurityException) {
                    e.printStackTrace()
                }
            }
        }
    }
}
