package com.singlelab.lume.ui.view.input

interface OnInvalidStringsListener {
    fun onInvalidString(view: InputView)

    fun onCorrectString(view: InputView)
}