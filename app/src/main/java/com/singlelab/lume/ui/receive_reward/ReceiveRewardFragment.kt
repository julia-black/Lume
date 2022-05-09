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
import com.singlelab.lume.util.addCardNumMask
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

    override fun showSuccess() {
        showSnackbar(getString(R.string.promo_request_send), ToastType.SUCCESS)
        findNavController().popBackStack()
    }


    override fun showImages(images: List<Bitmap>) {
        (recycler_images.adapter as ImageAdapter).setData(images.toMutableList())
    }

    override fun showEmptyCardNum() {
        showError(getString(R.string.empty_card_num))
    }

    override fun showInvalidCardNum() {
        showError(getString(R.string.not_valid_card_num))
    }

    override fun showEmptyImages() {
        showError(getString(R.string.empty_photo))
    }

    override fun onClickNewImage() {
        onClickAddImages(MAX_COUNT_IMAGES)
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
        edit_text_card_num.addCardNumMask()

        recycler_images.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = ImageAdapter(MAX_COUNT_IMAGES)
            (adapter as ImageAdapter).setClickListener(this@ReceiveRewardFragment)
        }
    }

    private fun setListener() {
        button_back.setOnClickListener {
            findNavController().popBackStack()
        }
        button_apply.setOnClickListener {
            presenter.onApplyReward(edit_text_card_num.text.toString())
        }
    }

    companion object {
        const val MAX_COUNT_IMAGES = 2
    }
}