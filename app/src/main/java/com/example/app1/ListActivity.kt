package com.example.app1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app1.ChatActivity
import com.example.app1.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class ListActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        firestore = FirebaseFirestore.getInstance()

        val scrollView = findViewById<ScrollView>(R.id.scrollViewUsernames)
        val container = findViewById<LinearLayout>(R.id.containerUsernames)

        fetchUsernames(container)
    }

    private fun fetchUsernames(container: LinearLayout) {
        firestore.collection("users")
            .get()
            .addOnSuccessListener { querySnapshot: QuerySnapshot ->
                container.removeAllViews() // Clear previous buttons
                if (!querySnapshot.isEmpty) {
                    for (document in querySnapshot) {
                        val username = document.getString("username")
                        if (username != null) {
                            addButton(username, container)
                        }
                    }
                } else {
                    Toast.makeText(this@ListActivity, "No users found.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error fetching usernames: ${exception.message}")
                Toast.makeText(this@ListActivity, "Error fetching users.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addButton(username: String, container: LinearLayout) {
        val button = Button(this).apply {
            text = username
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setOnClickListener {
                val intent = Intent(this@ListActivity, ChatActivity::class.java)
                intent.putExtra("recipientUsername", username)
                startActivity(intent)
            }
        }
        container.addView(button)
    }
}
