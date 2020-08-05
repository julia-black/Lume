package com.singlelab.lume.ui.chat.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.singlelab.lume.R
import com.singlelab.lume.util.generateImageLinkForPerson
import kotlinx.android.synthetic.main.group_incoming_message_item.view.*
import kotlinx.android.synthetic.main.outgoing_message_item.view.*


class GroupChatMessagesAdapter : ChatMessagesAdapter() {

    override fun setMessages(newMessages: List<ChatMessageItem>) {
        val diffResult = DiffUtil.calculateDiff(GroupChatMessagesDiffCallback(messages, newMessages), true)
        messages.clear()
        messages.addAll(newMessages)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun addMessage(newMessage: ChatMessageItem) {
        setMessages(messages + newMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ChatMessageItem.Type.INCOMING.code -> GroupIncomingMessageViewHolder(inflater.inflate(R.layout.group_incoming_message_item, parent, false))
            else -> GroupOutgoingMessageViewHolder(inflater.inflate(R.layout.outgoing_message_item, parent, false))
        }
    }

    override fun onBindViewHolder(holder: ChatMessageViewHolder, position: Int) =
        holder.bind(messages[position])

    class GroupIncomingMessageViewHolder(view: View) : ChatMessageViewHolder(view) {
        override fun bind(messageItem: ChatMessageItem) {
            if (messageItem !is GroupChatMessageItem) return

            itemView.incomingMessageView.text = messageItem.text
            itemView.incomingMessageAuthorView.text = messageItem.personName

            if (messageItem.personPhoto.isNotEmpty()) {
                Glide.with(itemView)
                    .load(messageItem.personPhoto.generateImageLinkForPerson())
                    .apply(RequestOptions.circleCropTransform())
                    .into(itemView.incomingMessagePhotoView)
            }

            val hasImages = messageItem.images.any { it.isNotEmpty() }
            itemView.incomingMessageImageView.isVisible = hasImages
            if (hasImages) {
                val url = if (messageItem.uid == "0") {
                    messageItem.images.first()
                } else {
                    messageItem.images.first().url
                }
                // TODO: Сделать отображение нескольких фото (view c +)
                val imagesCount = messageItem.images.size
                Glide.with(itemView)
                    .setDefaultRequestOptions(RequestOptions().timeout(30_000))
                    .load(url)
                    .into(itemView.incomingMessageImageView)
            }
        }
    }

    class GroupOutgoingMessageViewHolder(view: View) : ChatMessageViewHolder(view) {
        override fun bind(messageItem: ChatMessageItem) {

            itemView.outgoingMessageView.text = messageItem.text

            val hasImages = messageItem.images.any { it.isNotEmpty() }
            itemView.outgoingMessageImageView.isVisible = hasImages
            if (hasImages) {
                val url = if (messageItem.uid == "0") {
                    messageItem.images.first()
                } else {
                    messageItem.images.first().url
                }

                // TODO: Сделать отображение нескольких фото (view c +)
                val imagesCount = messageItem.images.size
                Glide.with(itemView)
                    .setDefaultRequestOptions(RequestOptions().timeout(30_000))
                    .load(url)
                    .into(itemView.outgoingMessageImageView)
            }
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