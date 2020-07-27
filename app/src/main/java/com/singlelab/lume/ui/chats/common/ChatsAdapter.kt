package com.singlelab.lume.ui.chats.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.singlelab.lume.R
import com.singlelab.lume.util.generateImageLinkForPerson
import com.singlelab.lume.util.isVisible
import kotlinx.android.synthetic.main.chats_item.view.*

class ChatsAdapter(
    private val onClickAction: (ChatItem) -> Unit,
    private val onLongClickAction: (ChatItem) -> Boolean
) : RecyclerView.Adapter<ChatsAdapter.ChatsItemViewHolder>() {

    private var chats = mutableListOf<ChatItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ChatsItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.chats_item, parent, false),
            onClickAction,
            onLongClickAction
        )

    override fun onBindViewHolder(holder: ChatsItemViewHolder, position: Int) =
        holder.bind(chats[position])

    override fun getItemCount() = chats.size

    fun setChats(chats: List<ChatItem>) {
        this.chats.clear()
        this.chats.addAll(chats)
    }

    class ChatsItemViewHolder(
        view: View,
        private val onClickAction: (ChatItem) -> Unit,
        private val onLongClickAction: (ChatItem) -> Boolean
    ) : RecyclerView.ViewHolder(view) {
        fun bind(chat: ChatItem) {
            itemView.chatsTitleView.text = chat.title
            itemView.chatsLastMessageView.isVisible = chat.lastMessage.isNotEmpty()
            itemView.chatsLastMessageView.text = chat.lastMessage

            // TODO: Понять какую картинку чата мы хотим отображать для групповых чатов
            // TODO: Подумать, может, стоит сделать для глайда интерсептор для хедеров, тогда, вероятно, стоить инжектить глайд
            /*if (chat.image.isNotEmpty()) {
                Glide.with(itemView)
                    .load(chat.image)
                    .into(itemView.chatsImageView)
            }*/

            itemView.setOnClickListener { onClickAction(chat) }
            itemView.setOnLongClickListener { onLongClickAction(chat) }
        }
    }
}