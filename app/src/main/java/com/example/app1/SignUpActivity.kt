package com.example.app1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Reference views
        val usernameEditText = findViewById<EditText>(R.id.username)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val signUpButton = findViewById<Button>(R.id.signUpButton)

        // Initialize Firestore
        val db = FirebaseFirestore.getInstance()

        // Handle sign-up button click
        signUpButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Both fields are required", Toast.LENGTH_SHORT).show()
            } else {
                // Check if the password is valid
                if (!isValidPassword(password)) {
                    Toast.makeText(this, "Password must be at least 6 characters long and contain 1 number and 1 special character", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Check if username already exists
                checkUsernameExists(username) { exists ->
                    if (exists) {
                        Toast.makeText(this, "Username already exists. Please choose another one.", Toast.LENGTH_SHORT).show()
                    } else {
                        // Create user data
                        val userData = hashMapOf(
                            "username" to username,
                            "password" to password
                        )

                        // Add the user to the "users" collection
                        db.collection("users")
                            .add(userData) // Firestore generates a unique document ID (userID)
                            .addOnSuccessListener { documentReference ->
                                val userId = documentReference.id // Auto-generated document ID
                                Toast.makeText(this, "Signup successful! User ID: $userId", Toast.LENGTH_SHORT).show()

                                // Redirect to MainActivity (Login Page)
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish() // Close current activity
                            }
                            .addOnFailureListener { exception ->
                                Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
            }
        }
    }

    // Function to check if username already exists in Firestore
    private fun checkUsernameExists(username: String, callback: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { result ->
                // If result is not empty, username exists
                callback(result.size() > 0)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error checking username: ${exception.message}", Toast.LENGTH_SHORT).show()
                callback(true) // Assume username is taken if there is an error
            }
    }

    // Function to validate the password
    private fun isValidPassword(password: String): Boolean {
        val regex = "^(?=.*[0-9])(?=.*[!@#\$%^&*(),.?\":{}|<>])[A-Za-z0-9!@#\$%^&*(),.?\":{}|<>]{6,}$"
        return password.matches(regex.toRegex())
    }
}
