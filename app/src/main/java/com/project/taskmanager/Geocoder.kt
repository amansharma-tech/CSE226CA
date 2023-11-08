package com.project.taskmanager

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class Geocoder : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_geocoder)

        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFE501")))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("Local Favourite Finder")

        val locationEditText = findViewById<EditText>(R.id.locationEditText)
        val openMapButton = findViewById<Button>(R.id.openMapButton)

        openMapButton.setOnClickListener {
            val location = locationEditText.text.toString()
            openGoogleMaps(location)
        }
    }

    private fun openGoogleMaps(location: String) {
        val uri = Uri.parse("geo:0,0?q=$location")
        val mapIntent = Intent(Intent.ACTION_VIEW, uri)
        mapIntent.setPackage("com.google.android.apps.maps") // Ensure Google Maps app is used
        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        } else {
            Toast.makeText(this, "Could not open Google maps ", Toast.LENGTH_LONG).show()
        }
    }
}
