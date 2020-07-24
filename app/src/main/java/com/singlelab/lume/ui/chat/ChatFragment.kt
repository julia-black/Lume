package com.singlelab.lume.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.base.OnlyForAuthFragments
import com.singlelab.lume.ui.chat.common.ChatMessageItem
import com.singlelab.lume.ui.chat.common.ChatMessagesAdapter
import com.singlelab.lume.ui.chat.common.ChatOpeningInvocationType
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

    private val chatMessagesAdapter: ChatMessagesAdapter by lazy { ChatMessagesAdapter() }

    private var chatType: ChatOpeningInvocationType? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_chat, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chatType = arguments?.let { ChatFragmentArgs.fromBundle(it).chatType }
        activity?.title = chatType?.title ?: getString(R.string.chat_title)

        chatView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        chatView.adapter = chatMessagesAdapter

        chatType?.let {
            presenter.showChat(it)
        }
    }

    override fun showChat(messages: List<ChatMessageItem>) {

    }

    override fun showEmptyChat() {
        // TODO: Сделать нормальный плейсхолер с сообщением, что нет чатов
        showError("Чат пуст")
    }
}