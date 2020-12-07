package com.singlelab.lume.ui.view.dialog

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.singlelab.lume.R
import kotlinx.android.synthetic.main.view_dialog.view.*


class DialogView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_dialog, this, true)
    }

    fun setTitle(title: String) {
        title_dialog.text = title
    }

    fun setPromoRules(
        @StringRes description: Int,
        @StringRes titleRules: Int,
        @StringRes rulesInfo: Int,
        @StringRes citiesInfo: Int,
        counterInfo: String? = null
    ) {
        description_dialog.setText(description)
        title_rules.setText(titleRules)
        rules.setText(rulesInfo)
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
        button_close.setOnClickListener {
            listener.onCloseDialogClick()
        }
    }

    fun setIcon(drawableId: Int) {
        icon_promo.setImageDrawable(ContextCompat.getDrawable(context, drawableId))
    }
}