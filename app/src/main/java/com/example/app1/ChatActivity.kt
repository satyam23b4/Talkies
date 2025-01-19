package com.example.app1

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app1.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.net.SocketException

class ChatActivity : AppCompatActivity() {

    private val messages = mutableListOf<String>()
    private lateinit var chatAdapter: ChatAdapter
    private var socket: Socket? = null
    private var output: PrintWriter? = null
    private var input: BufferedReader? = null

    private val serverAddress = "10.0.2.2"
    private val serverPort = 9999

    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private var recipientUsername: String? = null // Define as class property
    private lateinit var senderUsername: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewChat)
        val inputMessage: TextInputEditText = findViewById(R.id.inputMessage)
        val btnSend: FloatingActionButton = findViewById(R.id.btnSend)

        // Get the recipient and sender usernames
        recipientUsername = intent.getStringExtra("recipientUsername")
        senderUsername = intent.getStringExtra("senderUsername") ?: "Me"

        // Display the recipient's username in a Toast for debugging
        recipientUsername?.let {
            Toast.makeText(this, "Chatting with: $it", Toast.LENGTH_SHORT).show()
        }

        // Set up RecyclerView and adapter
        chatAdapter = ChatAdapter(messages, senderUsername)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = chatAdapter

        // Connect to the server
        connectToServer(senderUsername)

        // Handle send button click
        btnSend.setOnClickListener {
            val message = inputMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                val formattedMessage = message
                sendMessage(formattedMessage)
                messages.add("Me: $formattedMessage")
                chatAdapter.notifyItemInserted(messages.size - 1)
                recyclerView.scrollToPosition(messages.size - 1)
                inputMessage.text?.clear() // Clear input field
            }
        }
    }

    private fun connectToServer(senderUsername: String) {
        coroutineScope.launch {
            try {
                socket = Socket(serverAddress, serverPort)
                input = BufferedReader(InputStreamReader(socket!!.getInputStream()))
                output = PrintWriter(socket!!.getOutputStream(), true)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ChatActivity, "Connected to server", Toast.LENGTH_SHORT).show()
                }
                listenForMessages()
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ChatActivity,
                        "Error connecting to server: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun sendMessage(message: String) {
        coroutineScope.launch {
            try {
                output?.println(message)
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ChatActivity, "Error sending message", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun listenForMessages() {
        coroutineScope.launch {
            try {
                while (true) {
                    val incomingMessage = input?.readLine()
                    if (incomingMessage != null) {
                        withContext(Dispatchers.Main) {

                            if (recipientUsername != null) {
                                messages.add("$recipientUsername: $incomingMessage")
                            } else {
                                messages.add("Unknown: $incomingMessage")
                            }
                            chatAdapter.notifyItemInserted(messages.size - 1)
                            findViewById<RecyclerView>(R.id.recyclerViewChat)
                                .scrollToPosition(messages.size - 1)
                        }
                    }
                }
            } catch (e: SocketException) {
                // Handle socket closed exception
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ChatActivity, "Error receiving message", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel() // Cancel all coroutines
        try {
            socket?.close()
        } catch (e: Exception) {
            // Handle socket closing exception
        }
    }
}
