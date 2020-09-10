package com.singlelab.lume.ui.chat.common

import android.content.res.Resources
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.singlelab.lume.model.Const
import com.singlelab.lume.util.parse

interface ChatMessageViewExtensions {

    fun TextView.setMessageText(messageText: String) {
        isVisible = messageText.isNotEmpty()
        if (messageText.isNotEmpty()) {
            text = messageText
        }
    }

    fun TextView.setMessageTextViewDimensions(messageImages: List<String>, maxMessageViewWidth: Int) {
        if (messageImages.count { it.isNotEmpty() } > 0) {
            maxWidth = MESSAGE_TEXT_MAX_WIDTH.px
            setPadding(paddingLeft, paddingTop, 6.px, paddingBottom)
        } else {
            maxWidth = maxMessageViewWidth
            setPadding(paddingLeft, paddingTop, 32.px, paddingBottom)
        }
    }

    fun TextView.setMessageDate(messageText: String, messageDate: String) {
        isVisible = messageText.isNotEmpty()
        if (messageText.isNotEmpty()) {
            isVisible = messageDate.isNotEmpty()
            if (messageDate.isNotEmpty()) {
                text = messageDate.parse(Const.DATE_FORMAT_TIME_ZONE, Const.DATE_FORMAT_ONLY_TIME)
            }
        }
    }

    fun ImageView.setCloudView(messageText: String, messageImages: List<String>) {
        isVisible = messageImages.count { it.isNotEmpty() } == 0 && messageText.isNotEmpty()
    }

    val Int.px: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()

    companion object {
        // Нужно ставить под размер изображения
        const val MESSAGE_TEXT_MAX_WIDTH = 200
    }
}