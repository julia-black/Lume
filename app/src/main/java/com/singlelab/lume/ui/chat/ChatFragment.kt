package com.singlelab.lume.ui.chat

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nguyenhoanglam.imagepicker.model.Config.CREATOR.ROOT_DIR_DCIM
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.base.OnlyForAuthFragments
import com.singlelab.lume.base.listeners.OnActivityResultListener
import com.singlelab.lume.ui.chat.common.*
import com.singlelab.lume.ui.chat.common.ChatMessageItem.Companion.PENDING_MESSAGE_UID
import com.singlelab.lume.ui.chat.common.ChatMessageItem.Status
import com.singlelab.lume.ui.chat.common.ChatMessageItem.Type
import com.singlelab.lume.util.getBitmap
import com.singlelab.lume.util.toBase64
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

    private lateinit var chatType: ChatOpeningInvocationType

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_chat, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chatType = arguments?.let { ChatFragmentArgs.fromBundle(it).chatType } ?: return
        initViews()
        presenter.showChat(chatType)
    }

    override fun showChat(messages: List<ChatMessageItem>, page: Int) {
        emptyChatView.visibility = View.GONE
        chatMessagesAdapter.setMessages(messages)
        if (page == 1) {
            chatView.scrollToPosition(chatMessagesAdapter.itemCount - 1)
        }
    }

    override fun showEmptyChat() {
        emptyChatView.visibility = View.VISIBLE
    }

    override fun showNewMessage(message: ChatMessageItem) {
        emptyChatView.visibility = View.GONE
        chatMessagesAdapter.addMessage(message)
        chatView.scrollToPosition(chatMessagesAdapter.itemCount - 1)
    }

    override fun enableMessageSending(isEnabled: Boolean) {
        sendMessageView.isEnabled = isEnabled
        attachmentMessageView.isEnabled = isEnabled
    }

    override fun onActivityResultFragment(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandleResult(requestCode, resultCode, data, SELECT_IMAGE_REQUEST_CODE)) {
            val images = ImagePicker.getImages(data).mapNotNull { it.uri.getBitmap(activity?.contentResolver) }
            if (resultCode == Activity.RESULT_OK && images.isNotEmpty()) {
                sendMessage(images)
            } else {
                Toast.makeText(context, getString(R.string.error_pick_image), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun initViews() {
        activity?.title = chatType.title
        chatMessagesAdapter = if (chatType.isGroup) GroupChatMessagesAdapter() else PrivateChatMessagesAdapter()
        chatView.adapter = chatMessagesAdapter
        chatView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false).apply { stackFromEnd = true; }
        chatView.addItemDecoration(SpaceDivider(4))
        sendMessageView.setOnClickListener { sendMessage() }
        attachmentMessageView.setOnClickListener { addAttachment() }
        chatView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            (layoutManager as LinearLayoutManager).stackFromEnd = true
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    (layoutManager as LinearLayoutManager?)?.let { layoutManager ->
                        if (dy < 0 && presenter.isNeedLoading() && layoutManager.findFirstVisibleItemPosition() == 0) {
                            presenter.showChat(chatType, ++presenter.page)
                        }
                    }
                }
            })
        }
    }

    private fun sendMessage(images: List<Bitmap> = emptyList()) {
        val currentText = messageInputView.text.toString().trim()
        if (currentText.isNotEmpty() || images.isNotEmpty()) {
            showPendingMessage(currentText, images)
            presenter.sendMessage(currentText, images)
            messageInputView.setText("")
        }
    }

    private fun addAttachment() {
        activity?.let {
            ImagePicker.with(it)
                .setFolderMode(true)
                .setFolderTitle("Lume")
                .setRootDirectoryName(ROOT_DIR_DCIM)
                .setDirectoryName("Lume Images")
                .setMultipleMode(true)
                .setShowNumberIndicator(true)
                .setMaxSize(SELECT_IMAGE_MAX_COUNT)
                .setLimitMessage(getString(R.string.chat_select_images_limit, SELECT_IMAGE_MAX_COUNT))
                .setRequestCode(SELECT_IMAGE_REQUEST_CODE)
                .start()
        }
    }

    private fun showPendingMessage(text: String, images: List<Bitmap>) {
        val pendingMessage = if (chatType.isGroup) {
            GroupChatMessageItem(
                uid = PENDING_MESSAGE_UID,
                text = text,
                type = Type.OUTGOING,
                images = images.map { it.toBase64(20) },
                status = Status.PENDING,
                date = "",
                personPhoto = "",
                personName = ""
            )
        } else {
            PrivateChatMessageItem(
                uid = PENDING_MESSAGE_UID,
                text = text,
                type = Type.OUTGOING,
                images = images.map { it.toBase64(20) },
                status = Status.PENDING,
                date = ""
            )
        }
        showNewMessage(pendingMessage)
    }

    companion object {
        private const val SELECT_IMAGE_MAX_COUNT = 10
        private const val SELECT_IMAGE_REQUEST_CODE = 101
    }
}