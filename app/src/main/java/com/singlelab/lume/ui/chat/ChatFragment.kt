package com.singlelab.lume.ui.chat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.nguyenhoanglam.imagepicker.model.Config.CREATOR.ROOT_DIR_DCIM
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.base.OnlyForAuthFragments
import com.singlelab.lume.base.listeners.OnActivityResultListener
import com.singlelab.lume.ui.chat.common.*
import com.singlelab.lume.ui.chat.common.ChatOpeningInvocationType.Common
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_chat.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

@AndroidEntryPoint
class ChatFragment : BaseFragment(), ChatView, OnlyForAuthFragments, OnActivityResultListener {
    @Inject
    lateinit var daggerPresenter: ChatPresenter

    @InjectPresenter
    lateinit var presenter: ChatPresenter

    @ProvidePresenter
    fun providePresenter() = daggerPresenter

    private lateinit var chatMessagesAdapter: ChatMessagesAdapter

    private var isChatGroup: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_chat, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chatType = arguments?.let { ChatFragmentArgs.fromBundle(it).chatType }
        isChatGroup = (chatType as? Common)?.isGroup ?: false
        activity?.title = chatType?.title ?: getString(R.string.chat_title)

        initViews()

        chatType?.let {
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

    override fun onActivityResultFragment(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SELECT_IMAGE_REQUEST_CODE) {
            val images = ImagePicker.getImages(data).map { it.uri.toString() }
            if (resultCode == Activity.RESULT_OK) {
                sendMessage(images)
            } else {
                Toast.makeText(context, getString(R.string.error_pick_image), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun initViews() {
        chatMessagesAdapter = if (isChatGroup) GroupChatMessagesAdapter() else PrivateChatMessagesAdapter()
        chatView.adapter = chatMessagesAdapter
        chatView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false).apply { stackFromEnd = true; }
        chatView.addItemDecoration(SpaceDivider(4))
        sendMessageView.setOnClickListener { sendMessage() }
        attachmentMessageView.setOnClickListener { addAttachment() }
    }

    private fun sendMessage(images: List<String> = emptyList()) {
        val currentText = messageInputView.text.toString().trim()
        if (currentText.isNotEmpty()) {
            val message = if (isChatGroup) {
                GroupChatMessageItem("0", currentText, ChatMessageItem.Type.OUTGOING, "", "")
            } else {
                PrivateChatMessageItem("0", currentText, ChatMessageItem.Type.OUTGOING)
            }
            chatMessagesAdapter.addMessage(message)
            chatMessagesAdapter.notifyDataSetChanged()
            chatView.scrollToPosition(chatMessagesAdapter.itemCount - 1)
            presenter.sendMessage(currentText, images)
            messageInputView.setText("")
        }
    }

    private fun addAttachment() {
        ImagePicker.with(this)
            .setFolderMode(true)
            .setFolderTitle("Lume")
            .setRootDirectoryName(ROOT_DIR_DCIM)
            .setDirectoryName("Lume Images")
            .setMultipleMode(true)
            .setShowNumberIndicator(true)
            .setMaxSize(SELECT_IMAGE_MAX_COUNT)
            .setLimitMessage(getString(R.string.chat_select_images_limit, SELECT_IMAGE_MAX_COUNT))
            //.setSelectedImages(images)
            .setRequestCode(SELECT_IMAGE_REQUEST_CODE)
            .start()
    }


    companion object {
        private const val SELECT_IMAGE_MAX_COUNT = 10
        private const val SELECT_IMAGE_REQUEST_CODE = 100
    }
}