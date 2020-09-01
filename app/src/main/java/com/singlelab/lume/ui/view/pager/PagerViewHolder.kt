package com.singlelab.lume.ui.view.pager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.singlelab.lume.R
import kotlinx.android.synthetic.main.item_pager_menu.view.*

class PagerViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_pager_menu, parent, false)) {

    fun bind(view: PagerTabView) {
        if (view.getView().parent != null) {
            (view.getView().parent as ViewGroup).removeView(view.getView())
        }
        itemView.card_content.addView(
            view.getView(),
            0,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
    }
}