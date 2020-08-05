package com.singlelab.lume.ui.chat.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.singlelab.lume.R
import com.singlelab.lume.model.Const
import com.singlelab.lume.ui.chat.common.GroupChatMessagesAdapter.GroupIncomingMessageViewHolder
import com.singlelab.lume.ui.chat.common.GroupChatMessagesAdapter.GroupOutgoingMessageViewHolder
import com.singlelab.lume.ui.chat.common.PrivateChatMessagesAdapter.PrivateIncomingMessageViewHolder
import com.singlelab.lume.ui.chat.common.PrivateChatMessagesAdapter.PrivateOutgoingMessageViewHolder

abstract class ChatMessagesAdapter(
    private val chatType: Type
) : RecyclerView.Adapter<ChatMessagesAdapter.ChatMessageViewHolder>() {
    protected val messages = mutableListOf<ChatMessageItem>()

    override fun getItemViewType(position: Int) = messages[position].type.code

    override fun getItemCount() = messages.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        if (chatType == Type.GROUP) {
            return when (viewType) {
                ChatMessageItem.Type.INCOMING.code -> GroupIncomingMessageViewHolder(inflater.inflate(R.layout.group_incoming_message_item, parent, false))
                else -> GroupOutgoingMessageViewHolder(inflater.inflate(R.layout.outgoing_message_item, parent, false))
            }
        } else {
            return when (viewType) {
                ChatMessageItem.Type.INCOMING.code -> PrivateIncomingMessageViewHolder(inflater.inflate(R.layout.private_incoming_message_item, parent, false))
                else -> PrivateOutgoingMessageViewHolder(inflater.inflate(R.layout.outgoing_message_item, parent, false))
            }
        }

    }

    override fun onBindViewHolder(holder: ChatMessageViewHolder, position: Int) =
        holder.bind(messages[position])

    open fun setMessages(newMessages: List<ChatMessageItem>) {
        messages.clear()
        messages.addAll(newMessages)
    }

    open fun addMessage(newMessage: ChatMessageItem) {
        messages.add(newMessage)
    }

    abstract class ChatMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(messageItem: ChatMessageItem)

        protected fun TextView.setMessageText(message: String) {
            val isTextNotEmpty = message.isNotEmpty()
            isVisible = isTextNotEmpty
            if (isTextNotEmpty) {
                this.text = message
            }
        }

        protected fun ImageView.setImages(images: List<String>) {
            val hasImages = images.any { it.isNotEmpty() }
            isVisible = hasImages
            if (hasImages) {
                // TODO: Сделать отображение нескольких фото (view c +)
                val imagesCount = images.size
                Glide.with(this)
                    .setDefaultRequestOptions(RequestOptions().timeout(30_000))
                    .load(images.first().url)
                    .into(this)
            }
        }

        protected val String.url: String get() = "${Const.BASE_URL}image/get-chat-message-image?imageUid=$this"
    }

    enum class Type {
        PRIVATE,
        GROUP
    }
}