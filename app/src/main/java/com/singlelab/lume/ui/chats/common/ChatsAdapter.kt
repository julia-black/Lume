package com.singlelab.lume.ui.chats.common

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.singlelab.lume.R
import com.singlelab.lume.util.dpToPx
import com.singlelab.lume.util.generateImageLink
import com.singlelab.net.model.auth.AuthData
import kotlinx.android.synthetic.main.chats_item.view.*

class ChatsAdapter(
    private val onClickAction: (ChatItem) -> Unit
) : RecyclerView.Adapter<ChatsAdapter.ChatsItemViewHolder>() {

    private var chats = mutableListOf<ChatItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ChatsItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.chats_item, parent, false),
            onClickAction
        )

    override fun onBindViewHolder(holder: ChatsItemViewHolder, position: Int) =
        holder.bind(chats[position], position)

    override fun getItemCount() = chats.size

    fun setChats(chats: List<ChatItem>) {
        this.chats.clear()
        this.chats.addAll(chats)
    }

    class ChatsItemViewHolder(
        view: View,
        private val onClickAction: (ChatItem) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        fun bind(chat: ChatItem, position: Int) {
            itemView.chatsTitleView.text = chat.title
            itemView.chatsLastMessageView.isVisible = chat.lastMessage.isNotEmpty()
            val lastMessage = if (chat.isLastMessageImage) itemView.context.getString(R.string.chats_last_message_image) else chat.lastMessage
            itemView.chatsLastMessageView.isVisible = lastMessage.isNotEmpty()
            if (lastMessage.isNotEmpty()) {
                val lastMessageFull = if (chat.lastMessagePersonUid == AuthData.uid) {
                    itemView.context.getString(R.string.chats_last_message_author, lastMessage)
                } else {
                    chat.lastMessagePersonName + ": " + lastMessage
                }
                val lastMessageAuthorLength = lastMessageFull.length - lastMessage.length
                itemView.chatsLastMessageView.text = SpannableString(lastMessageFull).apply {
                    setSpan(ForegroundColorSpan(Color.BLACK), 0, lastMessageAuthorLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }

            if (chat.unreadMessagesCount > 0) {
                itemView.chatsUnreadCountMessagesView.text = chat.unreadMessagesCount.toString()
                itemView.chatsUnreadCountMessagesView.isVisible = true
                itemView.chatsContentContainer.background = itemView.context.getDrawable(R.drawable.chat_room_background)
            } else {
                itemView.chatsUnreadCountMessagesView.isVisible = false
                itemView.chatsContentContainer.background = null
            }

            if (chat.image.isNotEmpty()) {
                Glide.with(itemView)
                    .load(chat.image.generateImageLink())
                    .transform(CenterCrop(), RoundedCorners(10.dpToPx().toInt()))
                    .into(itemView.chatsImageView)
            } else {
                Glide.with(itemView)
                    .load(R.mipmap.image_event_default)
                    .transform(CenterCrop(), RoundedCorners(10.dpToPx().toInt()))
                    .into(itemView.chatsImageView)
            }

            itemView.setOnClickListener { onClickAction(chat) }
        }
    }
}