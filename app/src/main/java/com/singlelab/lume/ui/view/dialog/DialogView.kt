package com.singlelab.lume.ui.view.dialog

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
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

    fun setDescription(description: String) {
        description_dialog.text = description
    }

    fun setDialogListener(listener: OnDialogViewClickListener) {
        button_close.setOnClickListener {
            listener.onCloseDialogClick()
        }
    }
}