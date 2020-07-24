package com.singlelab.lume.ui.view.image_person

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.singlelab.lume.model.profile.Person

class ImagePersonAdapter(
    private val list: List<Person>,
    private val listener: OnPersonImageClickListener?
) : RecyclerView.Adapter<ImagePersonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagePersonViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ImagePersonViewHolder(
            inflater,
            parent
        )
    }

    override fun onBindViewHolder(holder: ImagePersonViewHolder, position: Int) {
        val person = list[position]
        holder.bind(person, listener)
    }

    override fun getItemCount(): Int = list.size

}