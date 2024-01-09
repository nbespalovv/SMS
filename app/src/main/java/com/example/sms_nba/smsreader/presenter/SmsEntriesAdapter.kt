package com.example.sms_nba.smsreader.presenter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.sms_nba.R
import com.example.sms_nba.databinding.SmsChatEntryItemBinding
import com.example.sms_nba.smsreader.data.SmsChatEntry

class SmsEntriesAdapter(
    private val onItemClick: (SmsChatEntry) -> Unit,
): RecyclerView.Adapter<SmsEntriesAdapter.SmsEntryViewHolder>() {

    private val list = mutableListOf<SmsChatEntry>()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SmsEntryViewHolder {
        val context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val binding = SmsChatEntryItemBinding.inflate(layoutInflater, parent, false)
        return SmsEntryViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: SmsEntryViewHolder, position: Int) {
        holder.bind(list[position])

    }

    override fun getItemCount(): Int =
        list.size

    fun submitList(list: List<SmsChatEntry>) = with(this.list) {
        clear()
        addAll(list)
        notifyDataSetChanged()
    }

    inner class SmsEntryViewHolder(
        private val binding: SmsChatEntryItemBinding,
        private val onItemClick: (SmsChatEntry) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(entry: SmsChatEntry) = with(binding) {
            val context = binding.root.context

            smsAvatar.background = getRandomBackground(context)
            smsAvatar.text= entry.adress.first().toString()
            smsSender.text= entry.adress
            messageText.text = entry.messages.first()

            root.setOnClickListener {
                onItemClick(entry)
            }
        }
    }

    private fun getRandomBackground (context: Context) : Drawable? {
        val background =
            ContextCompat.getDrawable(context, R.drawable.sms_chat_entry_avatar)
        val color = context.resources.getIntArray(R.array.rainbow).random()
        background?.let { DrawableCompat.setTint(it, color) }
        return background
    }

}