package com.singlelab.lume.ui.view.pager

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.singlelab.lume.R
import com.singlelab.lume.ui.view.pager.listener.OnSettingsClickListener
import kotlinx.android.synthetic.main.view_settings.view.*


class SettingsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var settingsClickListener: OnSettingsClickListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_settings, this, true)
    }

    fun setSettingsListener(listener: OnSettingsClickListener) {
        this.settingsClickListener = listener
        person_info.setOnClickListener {
            settingsClickListener?.onPersonInfoClick()
        }
        exit.setOnClickListener {
            settingsClickListener?.onLogoutClick()
        }
    }
}