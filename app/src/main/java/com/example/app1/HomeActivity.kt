package com.example.app1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initialize buttons
        val btnChatWithFriends: Button = findViewById(R.id.btnChatWithFriends)
        val btnReportToServer: Button = findViewById(R.id.btnReportToServer)

        // Set up click listeners
        btnChatWithFriends.setOnClickListener {
            // Navigate to ListActivity (for selecting a user to chat with)
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
        }

        btnReportToServer.setOnClickListener {
            // Navigate to ReportToServerActivity
            val intent = Intent(this, ReportActivity::class.java)
            startActivity(intent)
        }
    }
}
