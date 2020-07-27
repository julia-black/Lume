package com.singlelab.lume.ui.chat.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.singlelab.lume.R
import kotlinx.android.synthetic.main.incoming_message_item.view.*
import kotlinx.android.synthetic.main.outgoing_message_item.view.*

class ChatMessagesAdapter : RecyclerView.Adapter<ChatMessagesAdapter.ChatMessageViewHolder>() {

    private val messages = mutableListOf<ChatMessageItem>()

    override fun getItemViewType(position: Int) = messages[position].type.code

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ChatMessageItem.Type.INCOMING.code -> IncomingMessageViewHolder(inflater.inflate(R.layout.incoming_message_item, parent, false))
            else -> OutgoingMessageViewHolder(inflater.inflate(R.layout.outgoing_message_item, parent, false))
        }
    }

    override fun getItemCount() = messages.size

    override fun onBindViewHolder(holder: ChatMessageViewHolder, position: Int) =
        holder.bind(messages[position])

    fun setMessages(messages: List<ChatMessageItem>) {
        this.messages.clear()
        this.messages.addAll(messages)
    }

    fun addMessage(message: ChatMessageItem) {
        messages.add(message)
    }

    abstract class ChatMessageViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {
        abstract fun bind(messageItem: ChatMessageItem)
    }

    class IncomingMessageViewHolder(
        view: View
    ) : ChatMessageViewHolder(view) {
        override fun bind(messageItem: ChatMessageItem) {
            itemView.incomingMessageView.text = messageItem.text
            if (messageItem.personPhoto.isNotEmpty()) {
                Glide.with(itemView)
                    .load(messageItem.personPhoto)
                    .into(itemView.incomingMessagePhotoView)
            }
        }
    }

    class OutgoingMessageViewHolder(
        view: View
    ) : ChatMessageViewHolder(view) {
        override fun bind(messageItem: ChatMessageItem) {
            itemView.outgoingMessageView.text = messageItem.text
            if (messageItem.personPhoto.isNotEmpty()) {
                Glide.with(itemView)
                    .load(messageItem.personPhoto)
                    .into(itemView.outgoingMessagePhotoView)
            }
        }

    }
}