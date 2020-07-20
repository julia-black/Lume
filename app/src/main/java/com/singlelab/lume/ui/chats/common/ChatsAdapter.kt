package com.singlelab.lume.ui.chats.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.singlelab.data.model.chats.ChatsInfoItem
import com.singlelab.lume.R
import kotlinx.android.synthetic.main.chats_item.view.*

class ChatsAdapter : RecyclerView.Adapter<ChatsAdapter.ChatsItemViewHolder>() {

    private var chats = mutableListOf<ChatsInfoItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ChatsItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.chats_item, parent, false))

    override fun onBindViewHolder(holder: ChatsItemViewHolder, position: Int) =
        holder.bind(chats[position])

    override fun getItemCount(): Int = chats.size

    fun setChats(chats: List<ChatsInfoItem>) {
        this.chats.clear()
        this.chats.addAll(chats)
    }

    class ChatsItemViewHolder(chatItemView: View) : RecyclerView.ViewHolder(chatItemView) {
        fun bind(chat: ChatsInfoItem) {
            itemView.chatsTitleView.text = chat.title
            //view.setImage(chat.image)
            //val image = itemView.context.resources.getIdentifier("ic_baseline_image_50", "drawable", itemView.context.packageName)
            //view.setImage(image)
        }
    }
}