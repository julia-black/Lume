package com.singlelab.lume.ui.view.input

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.singlelab.lume.R
import kotlinx.android.synthetic.main.view_input.view.*


open class InputView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_input, this, true)
    }

    open fun setHint(hint: String) {
        text_input_layout.hint = hint
    }

    open fun setMaxLength(length: Int) {
        text_input_edit_text.filters = arrayOf<InputFilter>(LengthFilter(length))
    }

    open fun setInputType(inputType: Int) {
        text_input_edit_text.inputType = inputType
    }

    open fun setStartDrawable(drawable: Drawable) {
        text_input_layout.startIconDrawable = drawable
    }

    open fun setMaxLines(maxLines: Int) {
        text_input_edit_text.maxLines = maxLines
    }

    open fun setOnEditorListener(function: (Int) -> Boolean) {
        text_input_edit_text.setOnEditorActionListener { _, actionId, _ ->
            function.invoke(actionId)
        }
    }

    open fun getText() = text_input_edit_text.text.toString()

    open fun setText(text: String) {
        text_input_edit_text.setText(text)
    }

    open fun setError(s: String) {
        text_input_layout.error = s
    }
}