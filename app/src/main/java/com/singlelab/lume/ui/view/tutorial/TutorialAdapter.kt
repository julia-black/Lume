package com.singlelab.lume.ui.view.tutorial

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.singlelab.lume.model.tutorial.TutorialPage


class TutorialAdapter(
    private val list: MutableList<TutorialPage>
) : RecyclerView.Adapter<TutorialViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TutorialViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TutorialViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: TutorialViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun updateList(list: List<TutorialPage>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
}