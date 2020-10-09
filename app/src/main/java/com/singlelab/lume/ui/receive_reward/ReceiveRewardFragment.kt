package com.singlelab.lume.ui.receive_reward

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.base.listeners.OnActivityResultListener
import com.singlelab.lume.model.Const.SELECT_IMAGE_REQUEST_CODE
import com.singlelab.lume.model.view.ToastType
import com.singlelab.lume.ui.view.image.ImageAdapter
import com.singlelab.lume.ui.view.image.OnImageClickListener
import com.singlelab.lume.util.getBitmap
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_receive_reward.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

@AndroidEntryPoint
class ReceiveRewardFragment : BaseFragment(), ReceiveRewardView, OnImageClickListener,
    OnActivityResultListener {

    @Inject
    lateinit var daggerPresenter: ReceiveRewardPresenter

    @InjectPresenter
    lateinit var presenter: ReceiveRewardPresenter

    @ProvidePresenter
    fun providePresenter() = daggerPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_receive_reward, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            presenter.setEventUid(ReceiveRewardFragmentArgs.fromBundle(it).eventUid)
        }
        initViews()
        setListener()
    }

    override fun onActivityResultFragment(requestCode: Int, resultCode: Int, data: Intent?) {
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
                presenter.addImages(images)
            } else {
                showError(getString(R.string.error_pick_image))
            }
        }
    }

    override fun showSuccessSendFeedback() {
        showSnackbar(getString(R.string.feedback_send), ToastType.SUCCESS)
        findNavController().popBackStack()
    }

    override fun showEmptyFeedback() {
        showError(getString(R.string.empty_feedback))
    }

    override fun showImages(images: List<Bitmap>) {
        (recycler_images.adapter as ImageAdapter).setData(images.toMutableList())
    }

    override fun onClickNewImage() {
        onClickAddImages()
    }

    override fun onClickImage(position: Int) {
        showListDialog(
            getString(R.string.choose_action),
            arrayOf(
                getString(R.string.remove_image)
            ),
            DialogInterface.OnClickListener { _, which ->
                when (which) {
                    0 -> presenter.deleteImage(position)
                }
            }
        )
    }

    private fun initViews() {
        recycler_images.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = ImageAdapter()
            (adapter as ImageAdapter).setClickListener(this@ReceiveRewardFragment)
        }
        layout_card_num.apply {
            setHint(getString(R.string.hint_card_number))
            setSingleLine()
            setDigits("0123456789")
            setMaxLength(16)
        }
    }

    private fun setListener() {
        button_back.setOnClickListener {
            findNavController().popBackStack()
        }
        button_apply.setOnClickListener {
            val text = layout_card_num.getText()
            if (text.isEmpty()) {
                showSnackbar(getString(R.string.enter_fields), ToastType.ERROR)
            } else {
                presenter.onGiveFeedBackClick(text)
            }
        }
    }
}