package com.example.hugyourmug

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout

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

        FirebaseApp.initializeApp(this)

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

            Toast.makeText(this, "REGISTER CLICKED", Toast.LENGTH_SHORT).show()
            Log.d("REGISTER", "CLICKED")

            val firstName = edtFirstName.text.toString().trim()
            val lastName = edtLastName.text.toString().trim()
            val username = edtUsername.text.toString().trim()
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString()
            val confirmPassword = edtConfirmPassword.text.toString()

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
                Toast.makeText(this, "Invalid username", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isValidEmail(email)) {
                Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnRegister.isEnabled = false
            Toast.makeText(this, "Creating account...", Toast.LENGTH_SHORT).show()

            lifecycleScope.launch {
                try {
                    withTimeout(15000) {
                        val auth = FirebaseAuth.getInstance()
                        val db = FirebaseFirestore.getInstance()

                        Log.d("REGISTER", "Start createUserWithEmailAndPassword")
                        val authResult = auth.createUserWithEmailAndPassword(email, password).await()

                        val uid = authResult.user?.uid ?: throw Exception("User ID not found")
                        Log.d("REGISTER", "Auth success uid=$uid")

                        val data = hashMapOf(
                            "uid" to uid,
                            "firstName" to firstName,
                            "lastName" to lastName,
                            "username" to username,
                            "email" to email
                        )

                        Log.d("REGISTER", "Start Firestore set")
                        db.collection("users").document(uid).set(data).await()
                        Log.d("REGISTER", "Firestore set success")
                    }

                    Toast.makeText(
                        this@RegisterActivity,
                        "Account created successfully!",
                        Toast.LENGTH_SHORT
                    ).show()

                    finish()

                } catch (e: Exception) {
                    Log.e("REGISTER", "Registration failed", e)

                    FirebaseAuth.getInstance().currentUser?.delete()

                    Toast.makeText(
                        this@RegisterActivity,
                        e.message ?: "Registration failed",
                        Toast.LENGTH_LONG
                    ).show()

                } finally {
                    btnRegister.isEnabled = true
                }
            }
        }
    }
}
