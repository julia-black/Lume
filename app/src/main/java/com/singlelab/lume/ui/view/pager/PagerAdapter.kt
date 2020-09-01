package com.singlelab.lume.ui.view.pager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class PagerAdapter(private val list: MutableList<PagerTabView>) :
    RecyclerView.Adapter<PagerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PagerViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun updateBadgesView(badgesView: BadgesView) {
        list[1] = badgesView
    }

    fun updateFriendsView(friendsView: FriendsView) {
        list[0] = friendsView
    }
}