package com.singlelab.lume.ui.view.pager

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.singlelab.lume.R
import com.singlelab.lume.model.profile.Person
import com.singlelab.lume.model.view.PagerTab
import com.singlelab.lume.ui.view.image_person.OnPersonImageClickListener
import com.singlelab.lume.ui.view.pager.listener.OnFriendsClickListener
import com.singlelab.lume.ui.view.pager.listener.OnSettingsClickListener
import kotlinx.android.synthetic.main.view_bookmarks_pager.view.*


class PagerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var settingsView: SettingsView

    private var friendsView: FriendsView

    init {
        LayoutInflater.from(context).inflate(R.layout.view_bookmarks_pager, this, true)
        settingsView = SettingsView(context)
        friendsView = FriendsView(context)
        setListeners()
        selectTab(PagerTab.FRIENDS)
    }

    fun setSettingsListener(listener: OnSettingsClickListener) {
        settingsView.setSettingsListener(listener)
    }

    fun setFriendsListener(
        listener: OnFriendsClickListener,
        personImageListener: OnPersonImageClickListener
    ) {
        friendsView.setFriendsListener(listener, personImageListener)
    }

    fun setFriends(friends: List<Person>) {
        friendsView.setFriends(friends)
    }

    private fun setListeners() {
        card_tab_one.setOnClickListener {
            card_tab_one.setCardBackgroundColor(
                ContextCompat.getColor(context, R.color.colorWhite)
            )
            card_tab_two.setCardBackgroundColor(
                ContextCompat.getColor(context, R.color.colorTabNotSelected)
            )
            card_tab_three.setCardBackgroundColor(
                ContextCompat.getColor(context, R.color.colorTabNotSelected)
            )
            selectTab(PagerTab.FRIENDS)
        }

        card_tab_two.setOnClickListener {
            card_tab_one.setCardBackgroundColor(
                ContextCompat.getColor(context, R.color.colorTabNotSelected)
            )
            card_tab_two.setCardBackgroundColor(
                ContextCompat.getColor(context, R.color.colorWhite)
            )
            card_tab_three.setCardBackgroundColor(
                ContextCompat.getColor(context, R.color.colorTabNotSelected)
            )
            selectTab(PagerTab.BADGES)
        }

        card_tab_three.setOnClickListener {
            card_tab_one.setCardBackgroundColor(
                ContextCompat.getColor(context, R.color.colorTabNotSelected)
            )
            card_tab_two.setCardBackgroundColor(
                ContextCompat.getColor(context, R.color.colorTabNotSelected)
            )
            card_tab_three.setCardBackgroundColor(
                ContextCompat.getColor(context, R.color.colorWhite)
            )
            selectTab(PagerTab.SETTINGS)
        }
    }

    private fun selectTab(tab: PagerTab) {
        when (tab) {
            PagerTab.FRIENDS -> {
                showFriends()
            }
            PagerTab.BADGES -> {
                showBadges()
            }
            PagerTab.SETTINGS -> {
                showSettings()
            }
        }
    }

    private fun showFriends() {
        card_content.removeAllViews()
        card_content.addView(
            friendsView,
            0,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
    }

    private fun showBadges() {
        card_content.removeAllViews()
    }

    private fun showSettings() {
        card_content.removeAllViews()
        card_content.addView(
            settingsView,
            0,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
    }
}