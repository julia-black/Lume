package com.singlelab.lume.ui.chat.common

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import com.singlelab.lume.ui.chat.common.view.GroupChatIncomingMessageView

class GroupChatMessagesAdapter(
    clickEvent: OnMessageAuthorClickEvent
) : ChatMessagesAdapter(
    Type.GROUP,
    clickEvent
) {

    override fun setMessages(newMessages: List<ChatMessageItem>) {
        val syncedMessages = newMessages.syncLast()
        val diffResult = DiffUtil.calculateDiff(GroupChatMessagesDiffCallback(messages, syncedMessages), false)
        messages.clear()
        messages.addAll(syncedMessages)
        diffResult.dispatchUpdatesTo(this)
    }

    class GroupIncomingMessageViewHolder(
        view: View,
        private val clickEvent: OnMessageAuthorClickEvent? = null
    ) : ChatMessageViewHolder(view) {
        override fun bind(messageItem: ChatMessageItem) {
            if (messageItem !is GroupChatMessageItem) return
            if (itemView !is GroupChatIncomingMessageView) return
            itemView.setContent(messageItem, clickEvent)
        }
    }

    private class GroupChatMessagesDiffCallback(
        private val oldMessages: List<ChatMessageItem>,
        private val newMessages: List<ChatMessageItem>
    ) : ChatMessagesDiffCallback(oldMessages, newMessages) {

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldMessage = oldMessages[oldItemPosition] as? GroupChatMessageItem ?: return false
            val newMessage = newMessages[newItemPosition] as? GroupChatMessageItem ?: return false

            return super.areContentsTheSame(oldItemPosition, newItemPosition) &&
                    oldMessage.personName == newMessage.personName &&
                    oldMessage.personPhoto == newMessage.personPhoto
        }
    }
}