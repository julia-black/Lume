package com.singlelab.lume.ui.chat

import android.app.Activity
import android.content.*
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.base.OnlyForAuthFragments
import com.singlelab.lume.base.listeners.OnActivityResultListener
import com.singlelab.lume.model.Const.SELECT_IMAGE_REQUEST_CODE
import com.singlelab.lume.model.view.ToastType
import com.singlelab.lume.ui.chat.common.*
import com.singlelab.lume.ui.chat.common.ChatMessageItem.Companion.PENDING_MESSAGE_UID
import com.singlelab.lume.ui.chat.common.ChatMessageItem.Status
import com.singlelab.lume.ui.chat.common.ChatMessageItem.Type
import com.singlelab.lume.ui.chat.common.view.OnClickImageListener
import com.singlelab.lume.ui.chat.common.view.OnClickMessageListener
import com.singlelab.lume.util.getBitmap
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
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
        attachmentMessageView.isEnabled = true
        if (ImagePicker.shouldHandleResult(
                requestCode,
                resultCode,
                data,
                SELECT_IMAGE_REQUEST_CODE
            )
        ) {
            val images = ImagePicker.getImages(data)
                .mapNotNull { it.uri.getBitmap(activity?.contentResolver) }
            if (resultCode == Activity.RESULT_OK && images.isNotEmpty()) {
                sendMessage(images)
            } else {
                showError(getString(R.string.error_pick_image))
            }
        }
    }

    override fun navigateToPerson(personUid: String) {
        findNavController().navigate(ChatFragmentDirections.actionChatToPerson(personUid))
    }

    override fun showMuteDialog(isMuted: Boolean) {
        val dialogClickListener =
            DialogInterface.OnClickListener { _, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        presenter.muteChat()
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {
                    }
                }
            }
        val text = if (isMuted) R.string.on_push else R.string.mute_push
        showDialog(
            title = getString(text),
            listener = dialogClickListener
        )
    }

    override fun showMute(isMuted: Boolean) {
        context?.let { context ->
            val drawable = if (isMuted) R.drawable.ic_mute else R.drawable.ic_volume
            chatMuteView.setImageDrawable(ContextCompat.getDrawable(context, drawable))
        }
    }

    override fun navigateToEvent(eventUid: String) {
        findNavController().navigate(ChatFragmentDirections.actionChatToEvent(eventUid))
    }

    private fun initViews() {
        chatTitleView.text = chatType.title
        val listener = object : OnClickImageListener {
            override fun onClickImage(imageUids: List<String>) {
                navigateToImages(imageUids)
            }
        }
        val messageListener = object : OnClickMessageListener {
            override fun onLongClick(text: String) {
                showMessageAction(text)
            }
        }
        val clickEvent = object : OnMessageAuthorClickEvent {
            override fun invoke(personUid: String) {
                navigateToPerson(personUid)
            }
        }
        chatMessagesAdapter = if (chatType.isGroup) {
            GroupChatMessagesAdapter(clickEvent, listener, messageListener)
        } else {
            PrivateChatMessagesAdapter(listener, messageListener)
        }
        chatView.adapter = chatMessagesAdapter
        chatView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false).apply {
                stackFromEnd = true;
            }
        chatView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                (chatView.layoutManager as LinearLayoutManager?)?.let { layoutManager ->
                    if (dy < 0 && presenter.isNeedLoading() && layoutManager.findFirstVisibleItemPosition() == 0) {
                        presenter.showChat(chatType, ++presenter.page)
                    }
                }
            }
        })
        chatTitleView.setOnClickListener {
            presenter.onChatTitleClick()
        }
        chatView.addItemDecoration(SpaceDivider(16))
        chatBackView.setOnClickListener { findNavController().popBackStack() }
        sendMessageView.setOnClickListener { sendMessage() }
        attachmentMessageView.setOnClickListener {
            addAttachment()
            attachmentMessageView.isEnabled = false
        }
        chatMuteView.setOnClickListener {
            presenter.onChatMuteClick()
        }
    }

    private fun navigateToImages(imageUids: List<String>) {
        val action = ChatFragmentDirections.actionChatToImageSlider(imageUids.toTypedArray())
        findNavController().navigate(action)
    }

    private fun showMessageAction(text: String) {
        showListDialog(
            getString(R.string.choose_action),
            arrayOf(
                getString(R.string.copy_message)
            ), DialogInterface.OnClickListener { _, which ->
                when (which) {
                    0 -> copy(text)
                }
            }
        )
    }

    private fun copy(text: String) {
        val clipboard: ClipboardManager? =
            context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        val clip = ClipData.newPlainText("Сообщение", text)
        clipboard?.setPrimaryClip(clip)
        showSnackbar(getString(R.string.text_copied), ToastType.SUCCESS)
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
        onClickAddImages()
    }

    private fun showPendingMessage(text: String, images: List<Bitmap>) {
        val pendingMessage = if (chatType.isGroup) {
            GroupChatMessageItem(
                uid = PENDING_MESSAGE_UID,
                text = text,
                type = Type.OUTGOING,
                images = images.map { it.toString() },
                status = Status.PENDING,
                date = "",
                personUid = "",
                personPhoto = "",
                personName = ""
            )
        } else {
            PrivateChatMessageItem(
                uid = PENDING_MESSAGE_UID,
                text = text,
                type = Type.OUTGOING,
                images = images.map { it.toString() },
                status = Status.PENDING,
                date = ""
            )
        }
        showNewMessage(pendingMessage)
    }
}