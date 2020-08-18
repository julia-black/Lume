package com.singlelab.lume.ui.feedback

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.base.listeners.OnActivityResultListener
import com.singlelab.lume.ui.view.image.ImageAdapter
import com.singlelab.lume.ui.view.image.OnImageClickListener
import com.singlelab.lume.util.getBitmap
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_feedback.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

@AndroidEntryPoint
class FeedbackFragment : BaseFragment(), FeedbackView, OnImageClickListener,
    OnActivityResultListener {

    companion object {
        const val SELECT_IMAGE_MAX_COUNT = 10
        const val SELECT_IMAGE_REQUEST_CODE = 102
    }

    @Inject
    lateinit var daggerPresenter: FeedbackPresenter

    @InjectPresenter
    lateinit var presenter: FeedbackPresenter

    @ProvidePresenter
    fun providePresenter() = daggerPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feedback, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                Toast.makeText(context, getString(R.string.error_pick_image), Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun showSuccessSendFeedback() {
        showToast(getString(R.string.feedback_send))
        findNavController().popBackStack()
    }

    override fun showImages(images: List<Bitmap>) {
        (recycler_images.adapter as ImageAdapter).setData(images.toMutableList())
    }

    override fun onClickNewImage() {
        activity?.let {
            ImagePicker.with(it)
                .setFolderMode(true)
                .setFolderTitle("Lume")
                .setRootDirectoryName(Config.ROOT_DIR_DCIM)
                .setDirectoryName("Lume Images")
                .setMultipleMode(true)
                .setShowNumberIndicator(true)
                .setMaxSize(SELECT_IMAGE_MAX_COUNT)
                .setLimitMessage(
                    getString(
                        R.string.chat_select_images_limit,
                        SELECT_IMAGE_MAX_COUNT
                    )
                )
                .setRequestCode(SELECT_IMAGE_REQUEST_CODE)
                .start()
        }
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
            (adapter as ImageAdapter).setClickListener(this@FeedbackFragment)
        }
    }

    private fun setListener() {
        button_give_feedback.setOnClickListener {
            val text = feedback.text.toString()
            if (text.isEmpty()) {
                showError(getString(R.string.enter_fields))
            } else {
                presenter.onGiveFeedBackClick(text)
            }
        }
    }
}