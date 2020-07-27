package com.singlelab.lume.ui.swiper_person.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.singlelab.lume.model.profile.Person

class CardStackPersonAdapter(
    private var list: List<Person> = emptyList()
) : RecyclerView.Adapter<CardPersonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardPersonViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CardPersonViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: CardPersonViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setData(list: List<Person>) {
        this.list = list
        notifyDataSetChanged()
    }
}