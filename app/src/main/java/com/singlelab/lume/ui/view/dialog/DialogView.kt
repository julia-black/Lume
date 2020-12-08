package com.singlelab.lume.ui.view.dialog

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.text.toSpannable
import androidx.core.view.isVisible
import com.singlelab.lume.R
import com.singlelab.lume.util.handleUrlClicks
import kotlinx.android.synthetic.main.view_dialog.view.*


class DialogView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var listener: OnDialogViewClickListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_dialog, this, true)
    }

    fun setTitle(title: String) {
        title_dialog.text = title
    }

    fun setPromoRules(
        @StringRes description: Int,
        @StringRes titleRules: Int,
        @StringRes citiesInfo: Int,
        @StringRes rulesInfo: Int,
        @StringRes linkInRules: Int? = null,
        url: String? = null,
        counterInfo: String? = null
    ) {
        description_dialog.setText(description)
        title_rules.setText(titleRules)
        if (linkInRules == null || url == null) {
            rules.text =
                addBoldDigits(context.getString(rulesInfo).toSpannable() as SpannableString)
        } else {
            setLink(
                context.getString(linkInRules),
                context.getString(rulesInfo),
                url
            )
        }
        cities_info.setText(citiesInfo)
        if (counterInfo == null) {
            counter_info.isVisible = false
        } else {
            counter_info.isVisible = true
            counter_info.text = counterInfo
        }
    }

    fun setDescription(description: String) {
        description_dialog.text = description
    }

    fun setDialogListener(listener: OnDialogViewClickListener) {
        this.listener = listener
        button_close.setOnClickListener {
            listener.onCloseDialogClick()
        }
        rules.handleUrlClicks { url ->
            listener.onLinkClick(url)
        }
    }

    fun setIcon(drawableId: Int) {
        icon_promo.setImageDrawable(ContextCompat.getDrawable(context, drawableId))
    }

    private fun setLink(link: String, text: String, url: String) {
        val spannableResult = buildClickSpanString(
            SpannableString(text),
            link,
            url
        )
        rules.text = addBoldDigits(spannableResult)
        rules.movementMethod = LinkMovementMethod.getInstance()
        rules.setLinkTextColor(ContextCompat.getColor(context, R.color.colorPrimaryAccent))
    }

    private fun buildClickSpanString(
        spannableString: SpannableString,
        searchText: String,
        url: String
    ): SpannableString {
        val positions = Regex(searchText).findAll(spannableString).map { it.range.first }
        positions.forEach { pos ->
            if (pos >= 0) {
                val clickableSpan: ClickableSpan = object : ClickableSpan() {
                    override fun onClick(textView: View) {
                        listener?.onLinkClick(url)
                    }
                }
                spannableString.setSpan(
                    clickableSpan,
                    pos,
                    pos + searchText.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        return spannableString
    }

    private fun addBoldDigits(spannableString: SpannableString): SpannableString {
        val positions = Regex("\\d\\.").findAll(spannableString).map { it.range.first }
        positions.forEach { pos ->
            if (pos >= 0) {
                spannableString.setSpan(
                    StyleSpan(Typeface.BOLD),
                    pos,
                    pos + 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        return spannableString
    }
}