package com.example.app1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app1.R

class ChatAdapter(private val messages: List<String>, private val senderUsername: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVED = 2
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        return if (message.startsWith("Me:")) VIEW_TYPE_SENT else VIEW_TYPE_RECEIVED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SENT) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_sent, parent, false)
            SentMessageViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_received, parent, false)
            ReceivedMessageViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (holder is SentMessageViewHolder) {
            // Sent message: "Me: message"
            holder.textMessage.text = message
        } else if (holder is ReceivedMessageViewHolder) {
            // Received message: "$senderUsername: message"
            holder.textMessage.text = message
        }
    }

    override fun getItemCount(): Int = messages.size

    class SentMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textMessage: TextView = itemView.findViewById(R.id.textMessage)
    }

    class ReceivedMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textMessage: TextView = itemView.findViewById(R.id.textMessage)
    }
}
