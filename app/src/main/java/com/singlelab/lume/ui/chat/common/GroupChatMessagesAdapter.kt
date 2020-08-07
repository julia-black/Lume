package com.singlelab.lume.ui.chat.common

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.singlelab.lume.util.generateImageLinkForPerson
import kotlinx.android.synthetic.main.group_incoming_message_item.view.*

class GroupChatMessagesAdapter : ChatMessagesAdapter(Type.GROUP) {
    override fun setMessages(newMessages: List<ChatMessageItem>) {
        val syncedMessages = newMessages.syncLast()
        val diffResult = DiffUtil.calculateDiff(GroupChatMessagesDiffCallback(messages, syncedMessages), false)
        messages.clear()
        messages.addAll(syncedMessages)
        diffResult.dispatchUpdatesTo(this)
    }

    class GroupIncomingMessageViewHolder(view: View) : ChatMessageViewHolder(view) {
        override fun bind(messageItem: ChatMessageItem) {
            if (messageItem !is GroupChatMessageItem) return

            itemView.incomingMessageView.setMessageText(messageItem.text)
            itemView.incomingMessageImageView.setImages(messageItem.images)

            itemView.incomingMessageAuthorView.text = messageItem.personName

            if (messageItem.personPhoto.isNotEmpty()) {
                Glide.with(itemView)
                    .load(messageItem.personPhoto.generateImageLinkForPerson())
                    .apply(RequestOptions.circleCropTransform())
                    .into(itemView.incomingMessagePhotoView)
            }
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