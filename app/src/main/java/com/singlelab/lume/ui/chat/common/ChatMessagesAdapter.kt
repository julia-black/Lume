package com.singlelab.lume.ui.chat.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.singlelab.lume.R
import com.singlelab.lume.model.Const
import com.singlelab.lume.ui.chat.common.ChatMessageItem.Status
import com.singlelab.lume.ui.chat.common.GroupChatMessagesAdapter.GroupIncomingMessageViewHolder
import com.singlelab.lume.ui.chat.common.PrivateChatMessagesAdapter.PrivateIncomingMessageViewHolder
import com.singlelab.lume.ui.chat.common.view.ChatMessageImageView
import com.singlelab.lume.ui.chat.common.view.ChatOutgoingMessageView
import com.singlelab.lume.util.generateImageLink
import com.singlelab.lume.util.parse
import kotlinx.android.synthetic.main.chat_message_image_view.view.*

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
            ChatMessageItem.Type.OUTGOING.code -> OutgoingMessageViewHolder(ChatOutgoingMessageView(parent.context))
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

        protected fun ChatMessageImageView.setImage(message: ChatMessageItem) {
            val imagesCount = message.images.count { it.isNotEmpty() }
            val hasImages = imagesCount > 0
            isVisible = hasImages
            if (imagesCount == 1 && message.text.isEmpty()) {
                setMultipleImage(imagesCount)
                setDateChip(true, message.date)
                Glide.with(this)
                    .load(message.images.first().generateImageLink())
                    .transform(CenterCrop(), RoundedCorners(60))
                    .into(chatMessageImageView)
            } else if (hasImages) {
                setDateChip(false)
                setMultipleImage(imagesCount)
                Glide.with(this)
                    .load(message.images.first().generateImageLink())
                    .transform(CenterCrop(), GranularRoundedCorners(60f, 60f, 0f, 0f))
                    .into(chatMessageImageView)
            }
        }

        protected fun TextView.setDate(message: ChatMessageItem) {
            if (message.text.isNotEmpty()) {
                val isDateNotEmpty = message.date.isNotEmpty()
                if (isDateNotEmpty) {
                    isVisible = isDateNotEmpty
                    text = message.date.parse(Const.DATE_FORMAT_TIME_ZONE, "HH:mm")
                }
            } else {
                isVisible = false
            }
        }
    }

    class OutgoingMessageViewHolder(view: View) : ChatMessageViewHolder(view) {
        override fun bind(messageItem: ChatMessageItem) {
            if (itemView !is ChatOutgoingMessageView) return
            itemView.setContent(messageItem)
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
}