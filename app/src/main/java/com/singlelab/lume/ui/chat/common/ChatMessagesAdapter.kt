package com.singlelab.lume.ui.chat.common

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class ChatMessagesAdapter : RecyclerView.Adapter<ChatMessagesAdapter.ChatMessageViewHolder>() {
    protected val messages = mutableListOf<ChatMessageItem>()

    override fun getItemViewType(position: Int) = messages[position].type.code

    override fun getItemCount() = messages.size

    fun setMessages(messages: List<ChatMessageItem>) {
        this.messages.clear()
        this.messages.addAll(messages)
    }

    fun addMessage(message: ChatMessageItem) {
        messages.add(message)
    }

    abstract class ChatMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(messageItem: ChatMessageItem)
    }
}