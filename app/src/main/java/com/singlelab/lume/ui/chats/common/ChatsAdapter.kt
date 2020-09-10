package com.singlelab.lume.ui.chats.common

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.singlelab.lume.ui.chats.common.view.ChatItemView

class ChatsAdapter(
    private val onClickAction: OnChatClickEvent
) : RecyclerView.Adapter<ChatsAdapter.ChatsItemViewHolder>() {

    private var chats = mutableListOf<ChatItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ChatsItemViewHolder(ChatItemView(parent.context), onClickAction)

    override fun onBindViewHolder(holder: ChatsItemViewHolder, position: Int) =
        holder.bind(chats[position])

    override fun getItemCount() = chats.size

    fun setChats(newChats: List<ChatItem>) {
        chats.clear()
        chats.addAll(newChats)
    }

    class ChatsItemViewHolder(view: View, private val clickEvent: OnChatClickEvent) : RecyclerView.ViewHolder(view) {
        fun bind(chat: ChatItem) {
            if (itemView !is ChatItemView) return
            itemView.setContent(chat, clickEvent)
        }
    }
}