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
import com.bumptech.glide.request.RequestOptions
import com.singlelab.lume.R
import com.singlelab.lume.model.Const
import com.singlelab.lume.ui.chat.common.ChatMessageItem
import com.singlelab.lume.ui.chat.common.ChatMessageItem.Companion.PENDING_MESSAGE_UID
import com.singlelab.lume.ui.chat.common.GroupChatMessageItem
import com.singlelab.lume.util.generateImageLink
import com.singlelab.lume.util.parse
import kotlinx.android.synthetic.main.chat_message_image_view.view.*
import kotlinx.android.synthetic.main.group_incoming_message_item.view.*

class GroupChatIncomingMessageView
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
        inflate(getContext(), R.layout.group_incoming_message_item, this)
    }

    fun setContent(messageItem: GroupChatMessageItem) {
        incomingMessageTriangleView.isVisible = !(messageItem.hasImage && messageItem.text.isEmpty())
        setMessageTextMaxWidth(messageItem.hasImage)

        incomingMessageView.setMessageText(messageItem.text)
        incomingMessageImageView.setImage(messageItem)
        incomingMessageDateView.setDate(messageItem)

        incomingMessageAuthorView.isVisible = !messageItem.hasImage
        if (!messageItem.hasImage) {
            incomingMessageAuthorView.text = messageItem.personName
        }

        if (messageItem.personPhoto.isNotEmpty()) {
            Glide.with(this)
                .load(messageItem.personPhoto.generateImageLink())
                .apply(RequestOptions.circleCropTransform())
                .into(incomingMessageAuthorPhotoView)
        }
    }

    private val ChatMessageItem.hasImage: Boolean
        get() = images.count { it.isNotEmpty() } > 0

    private fun setMessageTextMaxWidth(withImage: Boolean) {
        if (withImage) {
            incomingMessageView.maxWidth = 200.px
            incomingMessageView.setPadding(incomingMessageView.paddingLeft, incomingMessageView.paddingTop, 6.px, incomingMessageView.paddingBottom)
        } else {
            incomingMessageView.setPadding(incomingMessageView.paddingLeft, incomingMessageView.paddingTop, 32.px, incomingMessageView.paddingBottom)
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
        if (message.uid == PENDING_MESSAGE_UID) {
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
}