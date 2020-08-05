package com.singlelab.lume.ui.creating_event.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class EventImagesAdapter :
    RecyclerView.Adapter<ImageViewHolder>() {

    private var images = mutableListOf<Bitmap?>()

    private var imageListener: OnImageClickListener? = null

    init {
        images.clear()
        images.add(null)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ImageViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(images[position], position, imageListener)
    }

    override fun getItemCount(): Int = images.size

    fun setClickListener(listener: OnImageClickListener) {
        this.imageListener = listener
    }

    fun addImage(image: Bitmap) {
        if (images.isNotEmpty()) {
            images.removeAt(images.size - 1)
        }
        images.add(image)
        addNewImageItem()
        notifyDataSetChanged()
    }

    fun setData(images: MutableList<Bitmap>) {
        this.images.clear()
        this.images.addAll(images)
        addNewImageItem()
        notifyDataSetChanged()
    }

    private fun addNewImageItem() {
        this.images.add(null)
    }
}