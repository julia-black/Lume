package com.singlelab.lume.ui.cities.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.singlelab.lume.R
import com.singlelab.lume.model.city.City
import kotlinx.android.synthetic.main.item_city.view.*


class CitiesViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_city, parent, false)) {

    fun bind(city: City, listener: OnCityClickListener) {
        itemView.title.text = city.cityName
        itemView.setOnClickListener {
            listener.onCityClick(city)
        }
    }
}