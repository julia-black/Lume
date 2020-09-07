package com.singlelab.lume.ui.view.input

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputFilter.LengthFilter
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import com.singlelab.lume.R
import kotlinx.android.synthetic.main.view_input.view.*


class InputView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_input, this, true)
    }

    fun setHint(hint: String) {
        text_input_layout.hint = hint
    }

    fun setMaxLength(length: Int) {
        val newFilters = text_input_edit_text.filters.toMutableList()
        newFilters.add(LengthFilter(length))
        text_input_edit_text.filters = newFilters.toTypedArray()
    }

    fun setInputType(inputType: Int) {
        text_input_edit_text.inputType = inputType
    }

    fun setStartDrawable(drawable: Drawable?) {
        text_input_layout.startIconDrawable = drawable
    }

    fun setMaxLines(maxLines: Int) {
        text_input_edit_text.maxLines = maxLines
    }

    fun setLines(lines: Int) {
        text_input_edit_text.setLines(lines)
        if (lines > 1) {
            layout.background = resources.getDrawable(R.drawable.shape_big_input, context.theme)
        }
    }

    fun disableLineBreaks() {
        text_input_edit_text.isSingleLine = true
        text_input_edit_text.setHorizontallyScrolling(false)
    }

    fun enableLineBreaks() {
        text_input_edit_text.isSingleLine = false
    }

    fun setOnEditorListener(function: (Int) -> Boolean) {
        text_input_edit_text.setOnEditorActionListener { _, actionId, _ ->
            function.invoke(actionId)
        }
    }

    fun getText() = text_input_edit_text.text.toString()

    fun setText(text: String?) {
        text_input_edit_text.setText(text)
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

    fun setImeAction(imeAction: Int) {
        text_input_edit_text.imeOptions = imeAction
    }

    fun addTextChangedListener(watcher: TextWatcher) {
        text_input_edit_text.addTextChangedListener(watcher)
    }

    fun setDigits(digits: String) {
        val regex = Regex("[^$digits]")
        var newStr = ""

        text_input_edit_text.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                val str = s.toString()
                if (str.isEmpty()) {
                    text_input_edit_text.append(newStr)
                    newStr = ""
                } else if (str != newStr) {
                    newStr = str.replace(regex, "")
                    text_input_edit_text.setText("")
                }
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }

    fun setSingleLine() {
        text_input_edit_text.isSingleLine = true
    }

    fun setMultiLine() {
        text_input_edit_text.isSingleLine = false
    }

    fun requestEditTextFocus() {
        text_input_edit_text.isFocusableInTouchMode = true
        text_input_edit_text.requestFocus()
    }

    fun getEditText(): EditText = text_input_edit_text
}