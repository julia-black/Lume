package com.singlelab.lume.ui.view.input

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.google.android.material.textfield.TextInputEditText
import com.singlelab.lume.R
import kotlinx.android.synthetic.main.view_phone_input.view.*


class PhoneInputView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_phone_input, this, true)
        edit_text_phone.fixHintsForMeizu(edit_text_phone as TextInputEditText, edit_text_phone)
    }

    fun isValid() = edit_text_phone.isValid

    fun setHint(hint: String) {
        edit_text_phone.setCustomHint(hint)
    }

    fun setMaxLength(length: Int) {
        edit_text_phone.filters = arrayOf<InputFilter>(LengthFilter(length))
    }

    fun setInputType(inputType: Int) {
        edit_text_phone.inputType = inputType
    }

    fun setStartDrawable(drawable: Drawable) {
        text_input_layout.startIconDrawable = drawable
    }

    fun setMaxLines(maxLines: Int) {
        edit_text_phone.maxLines = maxLines
    }

    fun setOnEditorListener(function: (Int) -> Boolean) {
        edit_text_phone.setOnEditorActionListener { _, actionId, _ ->
            function.invoke(actionId)
        }
    }

    fun getText() = edit_text_phone.text.toString()

    fun setText(text: String) {
        edit_text_phone.setText(text)
    }

    fun setError(error: String) {
        text_error.text = error
        text_error.visibility = View.VISIBLE
        text_warning.visibility = View.GONE
    }

    fun setWarning(warning: String) {
        text_warning.text = warning
        text_error.visibility = View.GONE
        text_warning.visibility = View.VISIBLE
    }
}