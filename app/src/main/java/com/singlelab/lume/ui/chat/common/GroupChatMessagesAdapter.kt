package com.singlelab.lume.ui.chat.common

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.singlelab.lume.util.generateImageLinkForPerson
import kotlinx.android.synthetic.main.group_incoming_message_item.view.*
import kotlinx.android.synthetic.main.outgoing_message_item.view.*


class GroupChatMessagesAdapter : ChatMessagesAdapter(Type.GROUP) {
    override fun setMessages(newMessages: List<ChatMessageItem>) {
        val diffResult = DiffUtil.calculateDiff(GroupChatMessagesDiffCallback(messages, newMessages), true)
        messages.clear()
        messages.addAll(newMessages)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun addMessage(newMessage: ChatMessageItem) {
        setMessages(messages + newMessage)
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

    class GroupOutgoingMessageViewHolder(view: View) : ChatMessageViewHolder(view) {
        override fun bind(messageItem: ChatMessageItem) {
            itemView.outgoingMessageView.setMessageText(messageItem.text)
            itemView.outgoingMessageImageView.setImages(messageItem.images)
        }
    }

    private class GroupChatMessagesDiffCallback(
        private val oldMessages: List<ChatMessageItem>,
        private val newMessages: List<ChatMessageItem>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldMessages.size

        override fun getNewListSize() = newMessages.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldMessages[oldItemPosition].uid == newMessages[newItemPosition].uid

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldMessage = oldMessages[oldItemPosition] as? GroupChatMessageItem ?: return false
            val newMessage = newMessages[newItemPosition] as? GroupChatMessageItem ?: return false

            return oldMessage.type == newMessage.type &&
                    oldMessage.personName == newMessage.personName &&
                    oldMessage.text == newMessage.text &&
                    oldMessage.images == newMessage.images
        }
    }
}