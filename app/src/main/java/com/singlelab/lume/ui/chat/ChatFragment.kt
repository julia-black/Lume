package com.singlelab.lume.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.ui.chat.ChatView.Companion.CHAT_TITLE_BUNDLE_KEY
import com.singlelab.lume.ui.chat.common.ChatAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_chat.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

@AndroidEntryPoint
class ChatFragment : BaseFragment(), ChatView {
    @Inject
    lateinit var daggerPresenter: ChatPresenter

    @InjectPresenter
    lateinit var presenter: ChatPresenter

    @ProvidePresenter
    fun providePresenter() = daggerPresenter

    private val chatAdapter: ChatAdapter by lazy { ChatAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_chat, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = arguments?.getString(CHAT_TITLE_BUNDLE_KEY) ?: getString(R.string.chat_title)
        chatTextView.text = arguments?.getString(CHAT_TITLE_BUNDLE_KEY) ?: getString(R.string.chat_title)
        chatView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        chatView.adapter = chatAdapter
    }
}