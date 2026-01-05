package com.example.hugyourmug

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val edtEmail = findViewById<EditText>(R.id.edtEmailLogin)
        val edtPassword = findViewById<EditText>(R.id.edtPasswordLogin)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnGoToRegister = findViewById<Button>(R.id.btnGoToRegister)

        btnGoToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        btnLogin.setOnClickListener {

            btnLogin.isEnabled = false

            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Please enter email and password",
                    Snackbar.LENGTH_LONG
                )
                    .setBackgroundTint(getColor(R.color.coffee_brown))
                    .setTextColor(getColor(android.R.color.white))
                    .show()
                btnLogin.isEnabled = true
                return@setOnClickListener
            }

            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                .addOnFailureListener {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        it.message ?: "Login failed",
                        Snackbar.LENGTH_LONG
                    )
                        .setBackgroundTint(getColor(R.color.coffee_brown))
                        .setTextColor(getColor(android.R.color.white))
                        .show()
                    btnLogin.isEnabled = true
                }
        }
    }
}
