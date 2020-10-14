package com.singlelab.lume.ui.view.dialog

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.singlelab.lume.R
import kotlinx.android.synthetic.main.view_dialog.view.*


class InfoDialogView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_info_dialog, this, true)
    }

    fun setDialogListener(listener: OnDialogViewClickListener) {
        button_close.setOnClickListener {
            listener.onCloseDialogClick()
        }
    }
}