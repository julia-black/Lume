package com.singlelab.lume.ui.view.input

import android.text.Editable
import android.text.TextWatcher

class PhoneInputWatcher : TextWatcher {

    companion object {
        const val MASK_TEXT_LENGTH = 17
    }

    private var inAfterTextChanged = false

    fun setBlockTextChange(b: Boolean) {
        inAfterTextChanged = b
    }

    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
    }

    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
    }

    override fun afterTextChanged(editable: Editable) {
        if (!inAfterTextChanged) {
            inAfterTextChanged = true
            var digits = editable.toString()
            if (digits.length <= 2) {
                digits = "+7"
            } else {
                digits = digits.replace("\\D+".toRegex(), "")
                if (digits.isNotEmpty()) digits = digits.substring(1)
                digits = "+7($digits"
                if (digits.length > 6) {
                    digits = digits.substring(0, 6) + ") " + digits.substring(6)
                }
                if (digits.length > 11) {
                    digits = digits.substring(0, 11) + "-" + digits.substring(11)
                }
                if (digits.length > 14) {
                    digits = digits.substring(0, 14) + "-" + digits.substring(14)
                }
            }
            editable.replace(0, editable.length, digits)
            inAfterTextChanged = false
        }
    }
}