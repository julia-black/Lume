package com.singlelab.lume.util

import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.lang.ref.WeakReference


class TextInputDebounce(
    editText: EditText,
    isHandleEmptyString: Boolean = false,
    minimumSymbols: Int = -1,
    private val delayMillis: Long = 300L
) {
    private val textInputReference: WeakReference<EditText>?
    private val debounceHandler = Handler(Looper.getMainLooper())
    private var debounceCallback: DebounceCallback? = null
    private var debounceWorker: Runnable
    private val textWatcher: TextWatcher

    init {
        debounceWorker = DebounceRunnable("", null)
        textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let { query ->
                    if (query.isEmpty()) {
                        if (isHandleEmptyString) {
                            handleQuery(query)
                        }
                    } else {
                        if (minimumSymbols > -1) {
                            if (query.length >= minimumSymbols) {
                                handleQuery(query)
                            }
                        } else {
                            handleQuery(query)
                        }
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        }
        textInputReference = WeakReference(editText)
        textInputReference.get()?.addTextChangedListener(textWatcher)
    }

    private fun handleQuery(s: CharSequence) {
        debounceHandler.removeCallbacks(debounceWorker)
        this.debounceWorker = DebounceRunnable(s.toString(), debounceCallback)
        debounceHandler.postDelayed(debounceWorker, delayMillis)
    }

    fun watch(debounceCallback: (String) -> Unit) {
        this.debounceCallback = object : DebounceCallback {
            override fun onFinished(result: String) {
                debounceCallback(result)
            }
        }
    }

    fun unwatch() {
        if (textInputReference != null) {
            val editText = textInputReference.get()
            if (editText != null) {
                editText.removeTextChangedListener(textWatcher)
                textInputReference.clear()
                debounceHandler.removeCallbacks(debounceWorker)
            }
        }
    }

    private class DebounceRunnable internal constructor(
        private val result: String,
        private val debounceCallback: DebounceCallback?
    ) : Runnable {
        override fun run() {
            debounceCallback?.onFinished(result)
        }
    }

    interface DebounceCallback {
        fun onFinished(result: String)
    }
}