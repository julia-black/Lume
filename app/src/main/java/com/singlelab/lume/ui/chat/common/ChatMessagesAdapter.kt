package com.singlelab.lume.ui.chat.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.singlelab.lume.R
import com.singlelab.lume.model.Const
import com.singlelab.lume.ui.chat.common.ChatMessageItem.Status
import com.singlelab.lume.ui.chat.common.GroupChatMessagesAdapter.GroupIncomingMessageViewHolder
import com.singlelab.lume.ui.chat.common.PrivateChatMessagesAdapter.PrivateIncomingMessageViewHolder
import com.singlelab.lume.util.parse
import kotlinx.android.synthetic.main.outgoing_message_item.view.*

abstract class ChatMessagesAdapter(
    private val chatType: Type
) : RecyclerView.Adapter<ChatMessagesAdapter.ChatMessageViewHolder>() {

    enum class Type {
        PRIVATE,
        GROUP
    }

    protected val messages = mutableListOf<ChatMessageItem>()

    override fun getItemViewType(position: Int) = messages[position].type.code

    override fun getItemCount() = messages.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ChatMessageItem.Type.OUTGOING.code -> OutgoingMessageViewHolder(inflater.inflate(R.layout.outgoing_message_item, parent, false))
            else -> if (chatType == Type.GROUP) {
                GroupIncomingMessageViewHolder(inflater.inflate(R.layout.group_incoming_message_item, parent, false))
            } else {
                PrivateIncomingMessageViewHolder(inflater.inflate(R.layout.private_incoming_message_item, parent, false))
            }
        }
    }

    override fun onBindViewHolder(holder: ChatMessageViewHolder, position: Int) =
        holder.bind(messages[position])

    open fun setMessages(newMessages: List<ChatMessageItem>) {
        val syncedMessages = newMessages.syncLast()
        val diffResult = DiffUtil.calculateDiff(ChatMessagesDiffCallback(messages, syncedMessages), false)
        messages.clear()
        messages.addAll(syncedMessages)
        diffResult.dispatchUpdatesTo(this)
    }

    protected fun List<ChatMessageItem>.syncLast(): List<ChatMessageItem> {
        val messages = this.toMutableList()
        if (messages.size > 1) {
            val lastMessage = messages.last()
            val preLastMessage = messages[messages.size - 2]
            if (preLastMessage.status == Status.PENDING && lastMessage.status == Status.SYNCED) {
                messages.remove(preLastMessage)
                if (lastMessage.type != preLastMessage.type && lastMessage.text != preLastMessage.text && lastMessage.images.size != preLastMessage.images.size) {
                    messages.add(preLastMessage)
                }
            }
        }
        return messages
    }

    open fun addMessage(newMessage: ChatMessageItem) {
        setMessages(messages + newMessage)
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

        protected fun TextView.setDate(date: String) {
            val isDateNotEmpty = date.isNotEmpty()
            isVisible = isDateNotEmpty
            if (isDateNotEmpty) {
                this.text = date.parse(Const.DATE_FORMAT_TIME_ZONE, CHAT_MESSAGE_DATE_FORMAT_OUTPUT)
            }
        }

        private val String.url: String get() = "${Const.BASE_URL}image/get-image?imageUid=$this"
    }

    class OutgoingMessageViewHolder(view: View) : ChatMessageViewHolder(view) {
        override fun bind(messageItem: ChatMessageItem) {
            itemView.outgoingMessageView.setMessageText(messageItem.text)
            itemView.outgoingMessageImageView.setImages(messageItem.images)
            itemView.outgoingMessageDateView.setDate(messageItem.date)

            val isPending = messageItem.status == Status.PENDING
            val background = if (isPending) R.drawable.group_message_input_background else R.drawable.private_outgoing_message_input_background
            itemView.outgoingMessageImageProgressView.isVisible = isPending
            itemView.outgoingMessageContainerView.background = itemView.context.getDrawable(background)
        }
    }

    open class ChatMessagesDiffCallback(
        private val oldMessages: List<ChatMessageItem>,
        private val newMessages: List<ChatMessageItem>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldMessages.size

        override fun getNewListSize() = newMessages.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldMessages[oldItemPosition].uid == newMessages[newItemPosition].uid ||
                    oldMessages[oldItemPosition].uid == ChatMessageItem.PENDING_MESSAGE_UID

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldMessage = oldMessages[oldItemPosition]
            val newMessage = newMessages[newItemPosition]

            return oldMessage.type == newMessage.type &&
                    oldMessage.text == newMessage.text &&
                    oldMessage.images == newMessage.images &&
                    oldMessage.date == newMessage.date &&
                    oldMessage.status == newMessage.status
        }
    }

    companion object {
        private const val CHAT_MESSAGE_DATE_FORMAT_OUTPUT = "HH:mm"
    }
}