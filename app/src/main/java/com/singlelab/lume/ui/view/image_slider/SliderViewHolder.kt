package com.singlelab.lume.ui.view.image_slider

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.singlelab.lume.R
import kotlinx.android.synthetic.main.item_slider.view.*

class SliderViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_slider, parent, false)) {

    fun bind(linkImage: String) {
        Glide.with(itemView)
            .load(linkImage)
            .into(itemView.image)
    }
}