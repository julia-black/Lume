package com.singlelab.lume.ui.view

import android.content.Context
import android.os.Build
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.text.SpannableString
import android.text.TextUtils
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.View.OnTouchListener
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.singlelab.lume.R
import com.singlelab.lume.util.maskPhone
import java.util.*

class PhoneEditText : TextInputEditText, OnFocusChangeListener, OnTouchListener {

    companion object {
        const val MASK_HINT = "+7 (＿＿) ＿＿-＿-＿"
        private val SPAN_HINT = SpannableString(MASK_HINT)
    }

    var phoneInputWatcher: PhoneInputWatcher = PhoneInputWatcher()
    var bufferHint = ""
    private var customHint: String? = null

    val unmaskText: String
        get() {
            if (text != null) {
                val s = text.toString()
                if (!TextUtils.isEmpty(s)) {
                    val unmaskedText = s.replace("\\D+".toRegex(), "")
                    if (!TextUtils.isEmpty(unmaskedText)) {
                        return unmaskedText.substring(1).trim { it <= ' ' }
                    }
                }
            }
            return ""
        }

    val isEmpty = if (text.isNullOrEmpty()) true else text.toString() == "+7"

    val isValid: Boolean
        get() = unmaskText.length == 10

    fun fixHintsForMeizu(vararg editTexts: TextInputEditText) {
        val manufacturer = Build.MANUFACTURER.toUpperCase(Locale.US)
        if (manufacturer.contains(context.getString(R.string.meizu_model))) {
            for (editText in editTexts) {
                editText.hint = editText.hint
            }
        }
    }

    constructor(context: Context?) : super(context) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
        init(attrs)
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    override fun onFocusChange(view: View, b: Boolean) {
        if (text != null) {
            val s = text.toString()
            if (b) {
                if (TextUtils.isEmpty(s) || s.length < 2) {
                    setText("+7")
                    setSelection(s.length)
                    setHint(bufferHint)
                }
                requestFocus()
            } else {
                if (s == "+7") {
                    phoneInputWatcher.setBlockTextChange(true)
                    setText("")
                    setHint(MASK_HINT)
                    setSelection(0)
                    phoneInputWatcher.setBlockTextChange(false)
                }
            }
        }
    }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        onTouchEvent(motionEvent)
        if (text != null) {
            setSelection(text!!.length)
        }
        return true
    }

    fun setCustomHint(hint: String?) {
        customHint = hint
        super.setHint(hint)
    }

    private fun setMaskedText(text: String?) {
        phoneInputWatcher.setBlockTextChange(true)
        setText(text?.maskPhone())
        phoneInputWatcher.setBlockTextChange(false)
    }

    fun setMaskedText(text: String?, hint: String) {
        setMaskedText(text)
        bufferHint = hint
    }

    private fun init(attributeSet: AttributeSet?) {
        setHintTextColor(ContextCompat.getColor(context, R.color.colorGrayText))
        if (attributeSet != null) {
            val array =
                context.obtainStyledAttributes(attributeSet, R.styleable.MaskEditText)
            try {
                @ColorInt val defaultHintColor =
                    ContextCompat.getColor(context, R.color.colorGrayText)
                @ColorInt val hintColor =
                    array.getColor(R.styleable.MaskEditText_met_text_color_hint, defaultHintColor)
                setHintTextColor(hintColor)
            } finally {
                array.recycle()
            }
        }
        onFocusChangeListener = this
        setOnTouchListener(this)
        maxLines = 1
        setText("+7")
        filters = arrayOf<InputFilter>(LengthFilter(PhoneInputWatcher.MASK_TEXT_LENGTH))
        inputType = InputType.TYPE_CLASS_NUMBER
        keyListener = DigitsKeyListener.getInstance(" -+()1234567890")
        if (TextUtils.isEmpty(hint)) {
            bufferHint = ""
            if (customHint == null) {
                setHint(SPAN_HINT)
            } else {
                setHint(customHint!!)
            }
        } else bufferHint = hint.toString()
        addTextChangedListener(phoneInputWatcher)
    }

    private fun setHint(hint: String) {
        if (customHint == null) {
            super.setHint(hint)
        } else {
            super.setHint(customHint)
        }
    }
}
