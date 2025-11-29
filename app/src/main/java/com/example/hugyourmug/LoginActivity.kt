package com.example.hugyourmug

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.hugyourmug.data.AppDatabase
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val edtIdentifier = findViewById<EditText>(R.id.edtEmailLogin)
        val edtPassword = findViewById<EditText>(R.id.edtPasswordLogin)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnGoToRegister = findViewById<Button>(R.id.btnGoToRegister)

        btnGoToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        btnLogin.setOnClickListener {
            val identifier = edtIdentifier.text.toString().trim()
            val password = edtPassword.text.toString().trim()

            if (identifier.isEmpty() || password.isEmpty()) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Please enter email/username and password",
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
                    if (identifier.contains("@")) {
                        userDao.getUserByEmail(identifier)
                    } else {
                        userDao.getUserByUsername(identifier)
                    }
                }

                if (user == null) {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "No account found for this email or username",
                        Snackbar.LENGTH_LONG
                    )
                        .setBackgroundTint(getColor(R.color.coffee_brown))
                        .setTextColor(getColor(android.R.color.white))
                        .show()
                    return@launch
                }

                val enteredHash = hashPassword(password)

                if (enteredHash == user.passwordHash) {
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
