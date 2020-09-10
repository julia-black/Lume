package com.singlelab.lume.ui.chats.common.view

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.singlelab.lume.R
import com.singlelab.lume.ui.chats.common.ChatItem
import com.singlelab.lume.ui.chats.common.OnChatClickEvent
import com.singlelab.lume.util.dpToPx
import com.singlelab.lume.util.generateImageLink
import com.singlelab.net.model.auth.AuthData
import kotlinx.android.synthetic.main.chats_item.view.*

class ChatItemView
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(
    context,
    attrs,
    defStyleAttr
) {

    init {
        layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        isFocusable = true
        isClickable = true
        inflate(getContext(), R.layout.chats_item, this)
    }

    fun setContent(chat: ChatItem, clickEvent: OnChatClickEvent) {
        chatsTitleView.text = chat.title

        setLastMessage(chat)
        setUnreadMessageView(chat.unreadMessagesCount)
        setChatImage(chat.image)

        setOnClickListener { clickEvent(chat) }
    }

    private fun setLastMessage(chat: ChatItem) {
        val lastMessage = if (chat.isLastMessageImage) context.getString(R.string.chats_last_message_image) else chat.lastMessage
        chatsLastMessageView.isVisible = lastMessage.isNotEmpty()
        if (lastMessage.isNotEmpty()) {
            val lastMessageFull = if (chat.lastMessagePersonUid == AuthData.uid) {
                context.getString(R.string.chats_last_message_author, lastMessage)
            } else {
                chat.lastMessagePersonName + ": " + lastMessage
            }
            val lastMessageAuthorLength = lastMessageFull.length - lastMessage.length
            chatsLastMessageView.text = SpannableString(lastMessageFull).apply {
                setSpan(ForegroundColorSpan(Color.BLACK), 0, lastMessageAuthorLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
    }

    private fun setUnreadMessageView(unreadMessagesCount: Int) {
        val hasUnreadMessages = unreadMessagesCount > 0
        chatsUnreadCountMessagesView.isVisible = hasUnreadMessages
        if (hasUnreadMessages) {
            chatsUnreadCountMessagesView.text = unreadMessagesCount.toString()
            chatsContentContainer.background = context.getDrawable(R.drawable.chat_room_background)
        } else {
            chatsContentContainer.background = null
        }
    }

    private fun setChatImage(image: String) {
        if (image.isNotEmpty()) {
            Glide.with(this)
                .load(image.generateImageLink())
                .thumbnail(0.1f)
                .transform(CenterCrop(), RoundedCorners(CORNER_RADIUS.dpToPx().toInt()))
                .into(chatsImageView)
        } else {
            Glide.with(this)
                .load(R.mipmap.image_event_default)
                .thumbnail(0.1f)
                .transform(CenterCrop(), RoundedCorners(CORNER_RADIUS.dpToPx().toInt()))
                .into(chatsImageView)
        }
    }

    companion object {
        private const val CORNER_RADIUS = 10
    }
}