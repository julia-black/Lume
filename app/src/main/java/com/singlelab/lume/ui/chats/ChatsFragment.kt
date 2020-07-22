package com.singlelab.lume.ui.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.singlelab.data.model.chat.ChatInfo
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.ui.chat.ChatView.Companion.CHAT_TITLE_BUNDLE_KEY
import com.singlelab.lume.ui.chats.common.ChatsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_chats.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

@AndroidEntryPoint
class ChatsFragment : BaseFragment(), ChatsView {
    @Inject
    lateinit var daggerPresenter: ChatsPresenter

    @InjectPresenter
    lateinit var presenter: ChatsPresenter

    @ProvidePresenter
    fun providePresenter() = daggerPresenter

    private val onChatClicked: (ChatInfo) -> Unit = {
        navigateToChat(it.title)
    }

    private val onChatLongClicked: (ChatInfo) -> Boolean = {
        true
    }

    private val chatsAdapter: ChatsAdapter by lazy { ChatsAdapter(onChatClicked, onChatLongClicked) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_chats, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = getString(R.string.chats_title)
        chatsView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        chatsView.adapter = chatsAdapter
        chatsView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
    }

    override fun showChats(chats: List<ChatInfo>) {
        chatsAdapter.setChats(chats)
        chatsAdapter.notifyDataSetChanged()
    }

    private fun navigateToChat(chatTitle: String) {
        //TODO: Use androidx.core:core-ktx or write extension bundleOf()
        val chatTitleBundle = Bundle().apply { putString(CHAT_TITLE_BUNDLE_KEY, chatTitle) }
        Navigation.createNavigateOnClickListener(R.id.navigate_from_chats_to_chat, chatTitleBundle).onClick(view)
    }
}