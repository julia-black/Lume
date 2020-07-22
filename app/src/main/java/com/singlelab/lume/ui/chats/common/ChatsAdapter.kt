package com.singlelab.lume.ui.chats.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.singlelab.net.model.chat.ChatInfo
import com.singlelab.lume.R
import kotlinx.android.synthetic.main.chats_item.view.*

class ChatsAdapter(
    private val onClickAction: (ChatInfo) -> Unit,
    private val onLongClickAction: (ChatInfo) -> Boolean
) : RecyclerView.Adapter<ChatsAdapter.ChatsItemViewHolder>() {

    private var chats = mutableListOf<ChatInfo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ChatsItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.chats_item, parent, false),
            onClickAction,
            onLongClickAction
        )

    override fun onBindViewHolder(holder: ChatsItemViewHolder, position: Int) =
        holder.bind(chats[position])

    override fun getItemCount() = chats.size

    fun setChats(chats: List<ChatInfo>) {
        this.chats.clear()
        this.chats.addAll(chats)
    }

    class ChatsItemViewHolder(
        view: View,
        private val onClickAction: (ChatInfo) -> Unit,
        private val onLongClickAction: (ChatInfo) -> Boolean
    ) : RecyclerView.ViewHolder(view) {
        fun bind(chat: ChatInfo) {
            itemView.chatsTitleView.text = chat.title
            //view.setImage(chat.image)
            //val image = itemView.context.resources.getIdentifier("ic_baseline_image_50", "drawable", itemView.context.packageName)
            //view.setImage(image)

            itemView.setOnClickListener { onClickAction(chat) }
            itemView.setOnLongClickListener { onLongClickAction(chat) }
        }
    }
}