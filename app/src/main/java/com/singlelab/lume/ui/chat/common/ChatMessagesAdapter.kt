package com.singlelab.lume.ui.chat.common

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.singlelab.lume.ui.chat.common.ChatMessageItem.Status
import com.singlelab.lume.ui.chat.common.GroupChatMessagesAdapter.GroupIncomingMessageViewHolder
import com.singlelab.lume.ui.chat.common.PrivateChatMessagesAdapter.PrivateIncomingMessageViewHolder
import com.singlelab.lume.ui.chat.common.view.ChatOutgoingMessageView
import com.singlelab.lume.ui.chat.common.view.GroupChatIncomingMessageView
import com.singlelab.lume.ui.chat.common.view.PrivateChatIncomingMessageView

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        ChatMessageItem.Type.OUTGOING.code -> OutgoingMessageViewHolder(ChatOutgoingMessageView(parent.context))
        else -> if (chatType == Type.GROUP) {
            GroupIncomingMessageViewHolder(GroupChatIncomingMessageView(parent.context))
        } else {
            PrivateIncomingMessageViewHolder(PrivateChatIncomingMessageView(parent.context))
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