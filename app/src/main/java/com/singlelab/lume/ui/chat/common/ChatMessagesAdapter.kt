package com.singlelab.lume.ui.chat.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.singlelab.lume.R

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

    override fun onBindViewHolder(holder: ChatMessageViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    abstract class ChatMessageViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {

    }

    class IncomingMessageViewHolder(
        view: View
    ) : ChatMessageViewHolder(view) {

    }

    class OutgoingMessageViewHolder(
        view: View
    ) : ChatMessageViewHolder(view) {

    }
}