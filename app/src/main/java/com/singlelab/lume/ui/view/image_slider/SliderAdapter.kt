package com.singlelab.lume.ui.view.image_slider

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SliderAdapter(private val imageLinks: MutableList<String>) :
    RecyclerView.Adapter<SliderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SliderViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.bind(imageLinks[position])
    }

    override fun getItemCount(): Int = imageLinks.size

    fun deleteItem(position: Int) {
        imageLinks.removeAt(position)
        notifyDataSetChanged()
    }
}