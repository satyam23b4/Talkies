package com.example.app1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class ReportActivity : AppCompatActivity() {

    private val serverAddress = "10.0.2.2"
    private val serverPort = 8888
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        val inputProblem: EditText = findViewById(R.id.inputProblem)
        val btnSendReport: Button = findViewById(R.id.btnSendReport)

        btnSendReport.setOnClickListener {
            val problemMessage = inputProblem.text.toString().trim()
            if (problemMessage.isNotEmpty()) {
                sendProblemToServer(problemMessage)
            } else {
                Toast.makeText(this, "Please enter a problem to report.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendProblemToServer(message: String) {
        coroutineScope.launch {
            try {
                // Create a UDP socket
                val socket = DatagramSocket()
                val serverInetAddress = InetAddress.getByName(serverAddress)
                val buffer = message.toByteArray()
                val packet = DatagramPacket(buffer, buffer.size, serverInetAddress, serverPort)

                // Send the message
                socket.send(packet)

                // Optional: Receive acknowledgment from the server
                val responseBuffer = ByteArray(1024)
                val responsePacket = DatagramPacket(responseBuffer, responseBuffer.size)
                socket.receive(responsePacket) // Blocking call
                val responseMessage = String(responsePacket.data, 0, responsePacket.length)

                // Update UI with server response
                runOnUiThread {
                    Toast.makeText(this@ReportActivity, "Server response: $responseMessage", Toast.LENGTH_LONG).show()
                }
                socket.close()
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@ReportActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
