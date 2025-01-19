package com.example.app1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Reference views
        val usernameEditText = findViewById<EditText>(R.id.username)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val forgotPasswordText = findViewById<TextView>(R.id.forgotPassword)
        val signUpButton = findViewById<Button>(R.id.signUpButton)

        // Initialize Firestore
        val db = FirebaseFirestore.getInstance()

        // Handle login button click
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show()
            } else {
                // Check the credentials in Firestore
                db.collection("users")
                    .whereEqualTo("username", username)
                    .get()
                    .addOnSuccessListener { documents ->
                        if (documents.isEmpty) {
                            Toast.makeText(this, "Username not found", Toast.LENGTH_SHORT).show()
                        } else {

                            val document = documents.first()
                            val storedPassword = document.getString("password")

                            if (storedPassword == password) {

                                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, HomeActivity::class.java)  // Navigate to HomeActivity
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this, "Error verifying credentials: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        // Handle forgot password text click
        forgotPasswordText.setOnClickListener {
            Toast.makeText(this, "Forgot Password? Feature coming soon!", Toast.LENGTH_SHORT).show()
        }

        // Handle sign-up button click
        signUpButton.setOnClickListener {
            // Navigate to the SignUpActivity
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}
