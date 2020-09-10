package com.singlelab.lume.ui.chat.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import com.singlelab.lume.R
import com.singlelab.lume.ui.chat.common.ChatMessageViewExtensions
import com.singlelab.lume.ui.chat.common.ChatMessageViewExtensions.Companion.MESSAGE_TEXT_MAX_WIDTH
import com.singlelab.lume.ui.chat.common.PrivateChatMessageItem
import kotlinx.android.synthetic.main.private_incoming_message_item.view.*

class PrivateChatIncomingMessageView
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(
    context,
    attrs,
    defStyleAttr
), ChatMessageViewExtensions {

    private var maxMessageViewWidth: Int = 0

    init {
        layoutParams = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        inflate(getContext(), R.layout.private_incoming_message_item, this)
    }

    fun setContent(messageItem: PrivateChatMessageItem) {
        setText(messageItem)

        incomingMessageCloudView.setCloudView(messageItem.text, messageItem.images)
        incomingMessageDateView.setMessageDate(messageItem.text, messageItem.date)
        incomingMessageImageView.setImage(messageItem)
    }

    private fun setText(messageItem: PrivateChatMessageItem) {
        incomingMessageView.setMessageText(messageItem.text)
        if (incomingMessageView.maxWidth != MESSAGE_TEXT_MAX_WIDTH.px) {
            maxMessageViewWidth = incomingMessageView.maxWidth
        }
        incomingMessageView.setMessageTextViewDimensions(messageItem.images, maxMessageViewWidth)
    }
}