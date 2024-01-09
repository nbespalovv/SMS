package com.example.sms_nba.smsreader.presenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sms_nba.R
import com.example.sms_nba.smsreader.data.SmsChatEntry

class SmsContentAdapter : RecyclerView.Adapter<SmsContentAdapter.SmsContentViewHolder>() {

    private var messages: List<String> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmsContentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.sms_content, parent, false)
        return SmsContentViewHolder(view)
    }

    override fun onBindViewHolder(holder: SmsContentViewHolder, position: Int) {
        val message = messages[position]
        holder.bind(message)
    }

    override fun getItemCount(): Int = messages.size

    fun submitList(messages: List<String>) {
        this.messages = messages
        notifyDataSetChanged()
    }

    class SmsContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageText: TextView = itemView.findViewById(R.id.message_text)

        fun bind(message: String) {
            messageText.text = message
        }
    }
}