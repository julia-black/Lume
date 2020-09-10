package com.singlelab.lume.ui.chat.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.custom.sliderimage.logic.SliderImage
import com.singlelab.lume.R
import com.singlelab.lume.model.Const
import com.singlelab.lume.ui.chat.common.ChatMessageItem
import com.singlelab.lume.util.generateImageLink
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
        layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        inflate(getContext(), R.layout.chat_message_image_view, this)
    }

    fun setImage(message: ChatMessageItem) {
        if (message.uid == ChatMessageItem.PENDING_MESSAGE_UID) {
            // Для сообщения, которое ждет отправки, отображаем только область картинки, если в сообщении есть картинки
            isVisible = message.images.isNotEmpty()
            chatMessageImageView.isVisible = message.images.isNotEmpty()
            return
        }

        val imagesCount = message.images.count { it.isNotEmpty() }
        val hasImages = imagesCount > 0

        isVisible = hasImages

        // Если сообщение содержит больше одной картинки,
        // то показывем фейд поверх первой картинки и счетчик количества кортинок в сообщении
        setMultipleImage(imagesCount, message.text)

        if (hasImages) {
            // Если сообщение содержит только картинки без текста,
            // то отображаем дату поверх картинки в правом нижнем углу, чтобы не отрисовывать область облака
            setDateChip(message.text.isEmpty(), message.date)

            // Определяем углы закругления для картинкок в сообщении:
            // если сообщение содержит только картинки без текста, то делаем закругление по всем сторонам,
            // иначе - делаем закругление только по внешнему краю картинки
            val transformations = mutableListOf<BitmapTransformation>(CenterCrop())
            if (message.text.isEmpty()) {
                transformations.add(RoundedCorners(CORNER_RADIUS_16.toInt()))
            } else {
                transformations.add(GranularRoundedCorners(CORNER_RADIUS_16, CORNER_RADIUS_16, CORNER_RADIUS_0, CORNER_RADIUS_0))
            }
            Glide.with(this)
                .load(message.images.first().generateImageLink())
                .thumbnail(0.1f)
                .transform(*transformations.toTypedArray())
                .into(chatMessageImageView)
        }

        chatMessageImageView.setOnClickListener { showFullScreenImages(message.images) }
    }

    private fun setMultipleImage(imageCount: Int, text: String) {
        val isMultipleImages = imageCount > 1

        chatMessageImageCountView.isVisible = isMultipleImages
        chatMessageImageForegroundView.isVisible = isMultipleImages

        if (isMultipleImages) {
            chatMessageImageCountView.text = context.getString(R.string.chat_message_images_count, imageCount - 1)

            // Делаем закругление фейда аналогично закруглению картинки
            val topRadius = CORNER_RADIUS_16
            val bottomRadius = if (text.isEmpty()) CORNER_RADIUS_16 else CORNER_RADIUS_0

            Glide.with(this)
                .load(R.drawable.shape_chat_message_image_foreground)
                .transform(CenterCrop(), GranularRoundedCorners(topRadius, topRadius, bottomRadius, bottomRadius))
                .into(chatMessageImageForegroundView)
        }
    }

    private fun setDateChip(isMessageTextEmpty: Boolean, date: String? = null) {
        chatMessageImageDateChip.isVisible = isMessageTextEmpty
        if (isMessageTextEmpty && date != null && date.isNotEmpty()) {
            chatMessageImageDateChip.text = date.parse(Const.DATE_FORMAT_TIME_ZONE, Const.DATE_FORMAT_ONLY_TIME)
        }
    }

    private fun showFullScreenImages(images: List<String>) {
        SliderImage.openfullScreen(
            context = context,
            items = images.filter { it.isNotEmpty() }.map { it.generateImageLink() },
            position = 0
        )
    }

    companion object {
        private const val CORNER_RADIUS_16 = 16F
        private const val CORNER_RADIUS_0 = 0F
    }
}