package com.singlelab.lume.ui.view.checkbox

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.CompoundButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.singlelab.lume.R
import kotlinx.android.synthetic.main.view_checkbox.view.*


class CheckBoxView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_checkbox, this, true)
        text.setOnClickListener {
            checkbox.isChecked = !checkbox.isChecked
        }
    }

    fun setText(textStr: String) {
        text.text = textStr
    }

    fun setChecked(isChecked: Boolean) {
        checkbox.isChecked = isChecked
    }

    fun getChecked() = checkbox.isChecked

    fun setListener(listener: CompoundButton.OnCheckedChangeListener) {
        checkbox.setOnCheckedChangeListener(listener)
    }
}