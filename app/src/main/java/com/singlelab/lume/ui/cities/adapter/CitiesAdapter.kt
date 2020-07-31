package com.singlelab.lume.ui.cities.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.singlelab.lume.model.city.City


class CitiesAdapter(private val listener: OnCityClickListener) :
    RecyclerView.Adapter<CitiesViewHolder>() {

    private val list = mutableListOf<City>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CitiesViewHolder(
            inflater,
            parent
        )
    }

    override fun onBindViewHolder(holder: CitiesViewHolder, position: Int) {
        val city = list[position]
        holder.bind(city, listener)
    }

    override fun getItemCount(): Int = list.size

    fun setData(list: List<City>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
}