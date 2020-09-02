package com.singlelab.lume.ui.chat.common.view

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.singlelab.lume.R
import com.singlelab.lume.model.Const
import com.singlelab.lume.ui.chat.common.ChatMessageItem
import com.singlelab.lume.ui.chat.common.PrivateChatMessageItem
import com.singlelab.lume.util.generateImageLink
import com.singlelab.lume.util.parse
import kotlinx.android.synthetic.main.chat_message_image_view.view.*
import kotlinx.android.synthetic.main.private_incoming_message_item.view.*

class PrivateChatIncomingMessageView
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(
    context,
    attrs,
    defStyleAttr
) {

    init {
        layoutParams = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        inflate(getContext(), R.layout.private_incoming_message_item, this)
    }

    fun setContent(messageItem: PrivateChatMessageItem) {
        setMessageTextMaxWidth(messageItem.hasImage)

        incomingMessageView.setMessageText(messageItem.text)
        incomingMessageImageView.setImage(messageItem)
        incomingMessageDateView.setDate(messageItem)
    }

    private val ChatMessageItem.hasImage: Boolean
        get() = images.count { it.isNotEmpty() } > 0

    private fun setMessageTextMaxWidth(withImage: Boolean) {
        if (withImage) {
            incomingMessageView.maxWidth = MESSAGE_TEXT_MAX_WIDTH.px
        }
    }

    private fun TextView.setMessageText(message: String) {
        val isTextNotEmpty = message.isNotEmpty()
        isVisible = isTextNotEmpty
        if (isTextNotEmpty) {
            this.text = message
        }
    }

    private fun ChatMessageImageView.setImage(message: ChatMessageItem) {
        if (message.uid == ChatMessageItem.PENDING_MESSAGE_UID) {
            isVisible = message.images.isNotEmpty()
            return
        }
        val imagesCount = message.images.count { it.isNotEmpty() }
        val hasImages = imagesCount > 0
        isVisible = hasImages
        if (message.text.isEmpty()) {
            setMultipleImage(imagesCount)
            setDateChip(true, message.date)
            Glide.with(this)
                .load(message.images.first().generateImageLink())
                .transform(CenterCrop(), GranularRoundedCorners(14f, 14f, 14f, 14f))
                .into(chatMessageImageView)
        } else if (hasImages) {
            setDateChip(false)
            setMultipleImage(imagesCount)
            Glide.with(this)
                .load(message.images.first().generateImageLink())
                .transform(CenterCrop(), GranularRoundedCorners(14f, 14f, 0f, 0f))
                .into(chatMessageImageView)
        }
    }

    private fun TextView.setDate(message: ChatMessageItem) {
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

    private val Int.px: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()

    companion object {
        // Нужно ставить под размер изображения
        private const val MESSAGE_TEXT_MAX_WIDTH = 200
    }
}