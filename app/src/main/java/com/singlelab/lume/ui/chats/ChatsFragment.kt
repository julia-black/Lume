package com.singlelab.lume.ui.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.base.OnlyForAuthFragments
import com.singlelab.lume.ui.chat.common.ChatOpeningInvocationType
import com.singlelab.lume.ui.chat.common.ChatOpeningInvocationType.Common
import com.singlelab.lume.ui.chats.common.ChatItem
import com.singlelab.lume.ui.chats.common.ChatsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_chats.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

@AndroidEntryPoint
class ChatsFragment : BaseFragment(), ChatsView, OnlyForAuthFragments {
    @Inject
    lateinit var daggerPresenter: ChatsPresenter

    @InjectPresenter
    lateinit var presenter: ChatsPresenter

    @ProvidePresenter
    fun providePresenter() = daggerPresenter

    private val onChatClicked: (ChatItem) -> Unit = { chatInfo ->
        navigateToChat(chatInfo)
    }

    private val chatsAdapter: ChatsAdapter by lazy { ChatsAdapter(onChatClicked) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_chats, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = getString(R.string.chats_title)
        initViews()
    }

    override fun showChats(chats: List<ChatItem>) {
        chatsAdapter.setChats(chats)
        chatsAdapter.notifyDataSetChanged()
    }

    override fun showEmptyChats() {
        // TODO: Сделать нормальный плейсхолер с сообщением, что нет чатов
        showError("У вас нет активных чатов")
    }

    private fun navigateToChat(chat: ChatItem) =
        findNavController().navigate(
            ChatsFragmentDirections.actionFromChatsToChat(
                if (chat.isGroup) Common(chat.title, chat.uid) else ChatOpeningInvocationType.Person(chat.title, chat.personUid)
            )
        )

    private fun initViews() {
        chatsView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        chatsView.adapter = chatsAdapter
        chatsView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
    }
}