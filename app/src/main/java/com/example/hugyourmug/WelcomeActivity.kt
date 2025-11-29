package com.example.hugyourmug

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val btnStart = findViewById<Button>(R.id.btnStartJourney)

        btnStart.setOnClickListener {
            // Go to Login screen
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
