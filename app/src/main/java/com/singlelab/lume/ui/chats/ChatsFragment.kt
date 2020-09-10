package com.singlelab.lume.ui.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.base.OnlyForAuthFragments
import com.singlelab.lume.ui.chat.common.ChatOpeningInvocationType
import com.singlelab.lume.ui.chat.common.ChatsItemDecorator
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

    private val chatsAdapter: ChatsAdapter by lazy { ChatsAdapter { navigateToChat(it) } }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_chats, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chatsTitleView.text = getString(R.string.chats_title)
        initViews()
    }

    override fun showChats(chats: List<ChatItem>) {
        emptyChatsView.visibility = View.GONE
        chatsAdapter.setChats(chats)
        chatsAdapter.notifyDataSetChanged()
    }

    override fun showEmptyChats() {
        emptyChatsView.visibility = View.VISIBLE
    }

    private fun navigateToChat(chat: ChatItem) {
        findNavController().navigate(
            ChatsFragmentDirections.actionFromChatsToChat(
                ChatOpeningInvocationType.Common(chat.title, chat.uid, chat.isGroup)
            )
        )
    }

    private fun initViews() {
        chatsView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        chatsView.adapter = chatsAdapter
        ContextCompat.getDrawable(requireContext(), R.drawable.chats_item_divider)?.let { chatsView.addItemDecoration(ChatsItemDecorator(it)) }
    }
}