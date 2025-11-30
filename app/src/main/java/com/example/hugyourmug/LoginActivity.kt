package com.example.hugyourmug

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.hugyourmug.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.google.android.material.snackbar.Snackbar

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
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val db = AppDatabase.getDatabase(applicationContext)
                val userDao = db.userDao()

                val user = withContext(Dispatchers.IO) {
                    userDao.getUserByEmail(email)
                }

                if (user == null) {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "No account found for this email",
                        Snackbar.LENGTH_LONG
                    )
                        .setBackgroundTint(getColor(R.color.coffee_brown))
                        .setTextColor(getColor(android.R.color.white))
                        .show()
                    return@launch
                }

                val enteredHash = hashPassword(password)

                if (enteredHash == user.passwordHash) {
                    // ✅ Save logged in user ID for cart & favorites
                    val sharedPref = getSharedPreferences("userData", MODE_PRIVATE)
                    sharedPref.edit()
                        .putInt("loggedInUserId", user.id)
                        .apply()

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Incorrect password",
                        Snackbar.LENGTH_LONG
                    )
                        .setBackgroundTint(getColor(R.color.coffee_brown))
                        .setTextColor(getColor(android.R.color.white))
                        .show()
                }
            }
        }
    }
}
