package com.singlelab.lume.ui.creating_event.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.singlelab.lume.R
import kotlinx.android.synthetic.main.item_event_image.view.*


class ImageViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_event_image, parent, false)) {

    fun bind(bitmap: Bitmap?, position: Int, listener: OnImageClickListener?) {
        if (bitmap == null) {
            itemView.image_event.setImageResource(R.drawable.shape_border)
            itemView.setOnClickListener {
                listener?.onClickNewImage()
            }
        } else {
            itemView.image_event.setImageBitmap(bitmap)
            itemView.setOnClickListener {
                listener?.onClickDeleteImage(position)
            }
        }

    }
}