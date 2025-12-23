package com.example.hugyourmug

import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

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

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

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

            db.collection("users")
                .whereEqualTo("username", username)
                .limit(1)
                .get()
                .addOnSuccessListener { snap ->
                    if (!snap.isEmpty) {
                        Toast.makeText(
                            this@RegisterActivity,
                            "This username is already taken",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@addOnSuccessListener
                    }

                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener { result ->
                            val uid = result.user?.uid ?: run {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Registration failed, please try again",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@addOnSuccessListener
                            }

                            val data = hashMapOf(
                                "uid" to uid,
                                "firstName" to firstName,
                                "lastName" to lastName,
                                "username" to username,
                                "email" to email
                            )

                            db.collection("users")
                                .document(uid)
                                .set(data)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        "Account created successfully!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    finish()
                                }
                                .addOnFailureListener {
                                    auth.currentUser?.delete()
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        it.message ?: "Registration failed, please try again",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                this@RegisterActivity,
                                it.message ?: "Registration failed, please try again",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this@RegisterActivity,
                        it.message ?: "Registration failed, please try again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
}
