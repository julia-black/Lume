package com.singlelab.lume.ui.chat.common.view

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.singlelab.lume.R
import com.singlelab.lume.model.Const
import com.singlelab.lume.util.parse
import kotlinx.android.synthetic.main.chat_message_image_view.view.*

class ChatMessageImageView
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(
    context,
    attrs,
    defStyleAttr
) {

    init {
        inflate(getContext(), R.layout.chat_message_image_view, this)
    }

    fun setMultipleImage(imageCount: Int, topRadius: Float = 14f, bottomRadius: Float = 14f) {
        val isMultipleImages = imageCount > 1
        chatMessageImageCountView.isVisible = isMultipleImages
        chatMessageImageForegroundView.isVisible = isMultipleImages
        if (isMultipleImages) {
            chatMessageImageCountView.text = context.getString(R.string.chat_message_images_count, imageCount - 1)
            Glide.with(this)
                .load(R.drawable.shape_chat_message_image_foreground)
                .transform(CenterCrop(), GranularRoundedCorners(topRadius, topRadius, bottomRadius, bottomRadius))
                .into(chatMessageImageForegroundView)
        }
    }

    fun setDateChip(isVisible: Boolean, date: String? = null) {
        chatMessageImageDateChip.isVisible = isVisible
        if (isVisible && date != null && date.isNotEmpty()) {
            val dateText = date.parse(Const.DATE_FORMAT_TIME_ZONE, "HH:mm")
            chatMessageImageDateChip.text = dateText
        } else {
            chatMessageImageDateChip.isVisible = false
        }
    }
}