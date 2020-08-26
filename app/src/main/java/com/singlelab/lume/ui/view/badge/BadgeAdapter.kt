package com.singlelab.lume.ui.view.badge

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.singlelab.lume.model.profile.Badge


class BadgeAdapter(private val list: List<Badge>) : RecyclerView.Adapter<BadgeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BadgeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return BadgeViewHolder(
            inflater,
            parent
        )
    }

    override fun onBindViewHolder(holder: BadgeViewHolder, position: Int) {
        val badge = list[position]
        holder.bind(badge)
    }

    override fun getItemCount(): Int = list.size
}