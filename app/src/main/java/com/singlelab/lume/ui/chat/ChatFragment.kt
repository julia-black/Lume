package com.singlelab.lume.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.base.OnlyForAuthFragments
import com.singlelab.lume.ui.chat.common.*
import com.singlelab.lume.ui.chat.common.ChatOpeningInvocationType.Common
import com.singlelab.lume.ui.chat.common.ChatOpeningInvocationType.Person
import dagger.hilt.android.AndroidEntryPoint
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

    private lateinit var chatMessagesAdapter: ChatMessagesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_chat, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chatType = arguments?.let { ChatFragmentArgs.fromBundle(it).chatType }
        activity?.title = chatType?.title ?: getString(R.string.chat_title)

        chatType?.let {
            initViews((chatType as? Common)?.isGroup ?: false)
            presenter.showChat(it)
        }
    }

    override fun showChat(messages: List<ChatMessageItem>) {
        chatMessagesAdapter.setMessages(messages)
        chatMessagesAdapter.notifyDataSetChanged()
    }

    override fun showEmptyChat() {
        // TODO: Сделать нормальный плейсхолер с сообщением, что чат пуст
        showError("Чат пуст")
    }

    private fun initViews(isChatGroup: Boolean) {
        chatMessagesAdapter = if (isChatGroup) GroupChatMessagesAdapter() else PrivateChatMessagesAdapter()
        chatView.adapter = chatMessagesAdapter
        chatView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false).apply { stackFromEnd = true; }
        chatView.addItemDecoration(SpaceDivider(4))

        sendMessageView.setOnClickListener {
            val currentText = messageInputView.text.toString()
            if (currentText.isNotEmpty()) {
                val message = if (isChatGroup) {
                    GroupChatMessageItem("0", currentText, ChatMessageItem.Type.OUTGOING, "", "")
                } else {
                    PrivateChatMessageItem("0", currentText, ChatMessageItem.Type.OUTGOING)
                }
                chatMessagesAdapter.addMessage(message)
                chatMessagesAdapter.notifyDataSetChanged()
                presenter.sendMessage(currentText)
                messageInputView.setText("")
            }
        }
    }
}