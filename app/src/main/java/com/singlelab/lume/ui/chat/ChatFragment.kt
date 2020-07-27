package com.singlelab.lume.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.base.OnlyForAuthFragments
import com.singlelab.lume.model.auth.Auth
import com.singlelab.lume.ui.chat.common.ChatMessageItem
import com.singlelab.lume.ui.chat.common.ChatMessagesAdapter
import com.singlelab.lume.ui.chat.common.ChatOpeningInvocationType
import com.singlelab.lume.ui.chat.common.SpaceDivider
import com.singlelab.net.model.auth.AuthData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.chats_item.*
import kotlinx.android.synthetic.main.fragment_chat.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

@AndroidEntryPoint
class ChatFragment : BaseFragment(), ChatView, OnlyForAuthFragments {
    @Inject
    lateinit var daggerPresenter: ChatPresenter

    @InjectPresenter
    lateinit var presenter: ChatPresenter

    @ProvidePresenter
    fun providePresenter() = daggerPresenter

    private val chatMessagesAdapter: ChatMessagesAdapter by lazy { ChatMessagesAdapter() }

    private var chatType: ChatOpeningInvocationType? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_chat, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chatType = arguments?.let { ChatFragmentArgs.fromBundle(it).chatType }
        activity?.title = chatType?.title ?: getString(R.string.chat_title)

        chatView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false).apply { stackFromEnd = true; }
        chatView.adapter = chatMessagesAdapter
        chatView.addItemDecoration(SpaceDivider(4))

        chatType?.let {
            presenter.showChat(it)
        }

        sendMessageView.setOnClickListener {
            val currentText = messageInputView.text.toString()
            if (currentText.isNotEmpty()) {
                chatMessagesAdapter.addMessage(ChatMessageItem("0", currentText, false, ChatMessageItem.Type.OUTGOING, "", "Вы"))
                chatMessagesAdapter.notifyDataSetChanged()
                presenter.sendMessage(currentText)
                messageInputView.setText("")
            }
        }
    }

    override fun showChat(messages: List<ChatMessageItem>) {
        chatMessagesAdapter.setMessages(messages)
        chatMessagesAdapter.notifyDataSetChanged()
    }

    override fun showEmptyChat() {
        // TODO: Сделать нормальный плейсхолер с сообщением, что нет чатов
        showError("Чат пуст")
    }
}