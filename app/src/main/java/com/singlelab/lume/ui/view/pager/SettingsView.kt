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
) : LinearLayout(context, attrs, defStyleAttr), PagerTabView {

    private var settingsClickListener: OnSettingsClickListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_settings, this, true)
    }

    override fun getTitle() = context.getString(R.string.tab_settings)

    override fun getView() = this

    fun setSettingsListener(listener: OnSettingsClickListener) {
        this.settingsClickListener = listener
        feedback.setOnClickListener {
            settingsClickListener?.onFeedbackClick()
        }
        agreement.setOnClickListener {
            settingsClickListener?.onAgreementClick()
        }
        about_developer.setOnClickListener {
            settingsClickListener?.onAboutDeveloperClick()
        }
        exit.setOnClickListener {
            settingsClickListener?.onLogoutClick()
        }
    }
}