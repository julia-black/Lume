package com.singlelab.lume.ui.chat.common

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.singlelab.lume.model.Const

abstract class ChatMessagesAdapter : RecyclerView.Adapter<ChatMessagesAdapter.ChatMessageViewHolder>() {
    protected val messages = mutableListOf<ChatMessageItem>()

    override fun getItemViewType(position: Int) = messages[position].type.code

    override fun getItemCount() = messages.size

    open fun setMessages(newMessages: List<ChatMessageItem>) {
        messages.clear()
        messages.addAll(newMessages)
    }

    open fun addMessage(newMessage: ChatMessageItem) {
        messages.add(newMessage)
    }

    abstract class ChatMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(messageItem: ChatMessageItem)

        protected val String.url: String
            get() = "${Const.BASE_URL}image/get-chat-message-image?imageUid=$this"

    }
}