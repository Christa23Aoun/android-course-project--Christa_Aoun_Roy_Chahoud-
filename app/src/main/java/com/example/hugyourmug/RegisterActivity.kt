package com.example.hugyourmug

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.hugyourmug.data.AppDatabase
import com.example.hugyourmug.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {

    private fun isValidUsername(username: String): Boolean {
        val regex = Regex("^[A-Za-z0-9_]{3,20}$")
        return regex.matches(username)
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val edtFirstName = findViewById<EditText>(R.id.edtRegisterFirstName)
        val edtLastName = findViewById<EditText>(R.id.edtRegisterLastName)
        val edtUsername = findViewById<EditText>(R.id.edtRegisterUsername)
        val edtEmail = findViewById<EditText>(R.id.edtRegisterEmail)
        val edtPassword = findViewById<EditText>(R.id.edtRegisterPassword)
        val edtConfirmPassword = findViewById<EditText>(R.id.edtRegisterConfirmPassword)

        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val btnBackToLogin = findViewById<Button>(R.id.btnBackToLogin)

        btnBackToLogin.setOnClickListener {
            finish()
        }

        btnRegister.setOnClickListener {
            val firstName = edtFirstName.text.toString().trim()
            val lastName = edtLastName.text.toString().trim()
            val username = edtUsername.text.toString().trim()
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()
            val confirmPassword = edtConfirmPassword.text.toString().trim()

            if (firstName.isEmpty() ||
                lastName.isEmpty() ||
                username.isEmpty() ||
                email.isEmpty() ||
                password.isEmpty() ||
                confirmPassword.isEmpty()
            ) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isValidUsername(username)) {
                Toast.makeText(
                    this,
                    "Username must be 3-20 characters (letters, numbers, _)",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (!isValidEmail(email)) {
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val db = AppDatabase.getDatabase(applicationContext)
                val userDao = db.userDao()

                val existingEmail = withContext(Dispatchers.IO) {
                    userDao.getUserByEmail(email)
                }

                if (existingEmail != null) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "This email is already registered",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@launch
                }

                val existingUsername = withContext(Dispatchers.IO) {
                    userDao.getUserByUsername(username)
                }

                if (existingUsername != null) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "This username is already taken",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@launch
                }

                val user = User(
                    firstName = firstName,
                    lastName = lastName,
                    username = username,
                    email = email,
                    passwordHash = hashPassword(password)
                )

                val success = withContext(Dispatchers.IO) {
                    try {
                        userDao.insert(user)
                        true
                    } catch (e: SQLiteConstraintException) {
                        false
                    }
                }

                if (success) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Account created successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Registration failed, please try again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
