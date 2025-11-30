package com.example.hugyourmug

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ⭐ FIX: Auto-skip if user already logged in
        val prefs = getSharedPreferences("userData", MODE_PRIVATE)
        val userId = prefs.getInt("loggedInUserId", -1)

        if (userId != -1) {
            // User is logged in → go directly to main screen
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_welcome)

        val btnStart = findViewById<Button>(R.id.btnStartJourney)

        btnStart.setOnClickListener {
            // Go to Login screen
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
