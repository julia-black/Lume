package com.singlelab.lume.ui.view.pager

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.singlelab.lume.R
import com.singlelab.lume.model.profile.Badge
import com.singlelab.lume.ui.view.badge.BadgeAdapter
import kotlinx.android.synthetic.main.view_badges.view.*


class BadgesView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), PagerTabView {

    private val badges: MutableList<Badge> = mutableListOf()

    init {
        LayoutInflater.from(context).inflate(R.layout.view_badges, this, true)
    }

    override fun getTitle() = context.getString(R.string.tab_badges)

    override fun getView() = this

    fun setBadges(badges: List<Badge>) {
        this.badges.clear()
        this.badges.addAll(badges)
        recycler_badges.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            visibility = View.VISIBLE
            adapter = BadgeAdapter(this@BadgesView.badges)
        }
        showLoading(false)
    }

    fun showLoading(isShow: Boolean) {
        loading.isVisible = isShow
        recycler_badges.isVisible = !isShow
    }
}