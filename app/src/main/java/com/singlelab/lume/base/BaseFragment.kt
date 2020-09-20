package com.singlelab.lume.base

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import com.singlelab.lume.MainActivity
import com.singlelab.lume.R
import com.singlelab.lume.base.listeners.OnActivityResultListener
import com.singlelab.lume.base.listeners.OnPermissionListener
import com.singlelab.lume.base.view.ErrorView
import com.singlelab.lume.base.view.LoadingView
import com.singlelab.lume.model.Const
import com.singlelab.lume.model.profile.PersonNotifications
import com.singlelab.lume.model.view.ToastType
import com.singlelab.net.model.auth.AuthData
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_edit_dialog.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import moxy.MvpAppCompatFragment
import kotlin.coroutines.CoroutineContext

open class BaseFragment : MvpAppCompatFragment(), ErrorView, LoadingView {
    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private var snackbar: Snackbar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading(false)
        showBottomNavigation(isShow = !AuthData.isAnon)
        if (AuthData.isAnon && this is OnlyForAuthFragments) {
            //todo подумать, как можно получше сделать общий обработчик того, что если
            // пользователь не залогинен, то с некоторых экранов должен осуществляться переход на авторизацию
            toAuth()
        } else {
            if (this is OnActivityResultListener) {
                (activity as MainActivity?)?.setActivityListener(this)
            }
            if (this is OnPermissionListener) {
                (activity as MainActivity?)?.setPermissionListener(this)
            }
        }
    }

    override fun onStop() {
        hideKeyboard()
        hideSnackbar()
        super.onStop()
    }


    override fun showLoading(isShow: Boolean, withoutBackground: Boolean) {
        (activity as MainActivity?)?.showLoading(isShow, withoutBackground)
    }

    override fun showLoadingText(text: String) {
        (activity as MainActivity?)?.showLoadingText(text)
    }

    override fun showError(
        message: String,
        withRetry: Boolean,
        callRetry: () -> Unit
    ) {
        showSnackbar(
            message,
            if (withRetry) ToastType.ERROR_RETRY else ToastType.ERROR,
            callRetry
        )
    }

    override fun showError(messageId: Int, withRetry: Boolean, callRetry: () -> Unit) {
        showError(getString(messageId), withRetry, callRetry)
    }

    override fun toAuth() {
        showLoading(false)
        findNavController().popBackStack()
        findNavController().navigate(R.id.auth)
    }

    override fun showNotifications(notifications: PersonNotifications) {
        (activity as MainActivity).showNotifications(notifications)
    }

    protected fun invokeSuspend(block: suspend () -> Unit) {
        scope.launch { block.invoke() }
    }

    protected fun runOnMainThread(block: () -> Unit) {
        scope.launch(CoroutineContextProvider().main) {
            block.invoke()
        }
    }

    fun showKeyboard() {
        val imm: InputMethodManager =
            activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity?.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    fun hideKeyboard() {
        val imm: InputMethodManager =
            activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity?.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showDialog(title: String, text: String, listener: DialogInterface.OnClickListener) {
        context?.let {
            val builder = AlertDialog.Builder(it)
            builder
                .setTitle(title)
                .setMessage(text)
                .setPositiveButton(getString(R.string.yes), listener)
                .setNegativeButton(getString(R.string.no), listener)
                .show()
        }
    }

    fun showListDialog(
        title: String,
        list: Array<String>,
        listener: DialogInterface.OnClickListener
    ) {
        context?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(title)
            builder.setItems(list, listener)
            val dialog = builder.create()
            dialog.show()
        }
    }

    fun showEditTextDialog(text: String?, emptyText: String, callback: (String) -> Unit) {
        context?.let {
            val customLayout = layoutInflater.inflate(R.layout.view_edit_dialog, null)
            text?.let {
                customLayout.edit_text.setText(text)
            }
            customLayout.edit_text.requestFocus()

            val builder = AlertDialog.Builder(it)
            builder.setView(customLayout)
            builder.setTitle(getString(R.string.edit_description))
            builder.setPositiveButton(getString(R.string.apply_edit)) { _, _ ->
                if (customLayout.edit_text.text.isNullOrEmpty()) {
                    showSnackbar(emptyText)
                } else {
                    callback.invoke(customLayout.edit_text.text.toString())
                }
            }

            builder.setNegativeButton(
                getString(R.string.cancel_action)
            ) { _, _ -> }
            builder.show()
        }
    }

    fun showBottomNavigation(isShow: Boolean) {
        (activity as MainActivity).showBottomNavigation(isShow)
    }

    fun onClickChangeImage() {
        activity?.let { activity ->
            CropImage.activity()
                .setFixAspectRatio(true)
                .setRequestedSize(
                    Const.IMAGE_RESOLUTION_WIDTH,
                    Const.IMAGE_RESOLUTION_HEIGHT,
                    CropImageView.RequestSizeOptions.RESIZE_FIT
                )
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .start(activity)
        }
    }

    fun onClickAddImages() {
        activity?.let {
            ImagePicker.with(it)
                .setFolderMode(true)
                .setFolderTitle(Const.APP_NAME)
                .setRootDirectoryName(Config.ROOT_DIR_DCIM)
                .setDirectoryName(Const.FOLDER_NAME)
                .setMultipleMode(true)
                .setShowNumberIndicator(true)
                .setDoneTitle(getString(R.string.choose))
                .setMaxSize(Const.MAX_COUNT_IMAGES)
                .setLimitMessage(
                    getString(
                        R.string.chat_select_images_limit,
                        Const.MAX_COUNT_IMAGES
                    )
                )
                .setRequestCode(Const.SELECT_IMAGE_REQUEST_CODE)
                .start()
        }
    }

    fun openBrowser(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    fun shareText(text: String) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, text)
        sendIntent.type = "text/plain"
        startActivity(Intent.createChooser(sendIntent, getString(R.string.share)))
    }

    fun showSnackbar(
        message: String,
        type: ToastType = ToastType.WARNING,
        callRetry: () -> Unit = {}
    ) {
        context?.let {
            val snackbar = Snackbar.make(requireActivity().container, message, type.length)
            val snackbarLayout = snackbar.view

            snackbarLayout.elevation = 0f
            snackbarLayout.backgroundTintList = ContextCompat.getColorStateList(it, type.colorResId)

            val textView = snackbarLayout.findViewById<View>(R.id.snackbar_text) as TextView

            if (type.drawableResId != null) {
                textView.setCompoundDrawablesWithIntrinsicBounds(type.drawableResId, 0, 0, 0)
                textView.compoundDrawablePadding =
                    resources.getDimension(R.dimen.margin_small).toInt()
            }
            if (type == ToastType.ERROR_RETRY) {
                this.snackbar = snackbar
            }
            snackbarLayout.setOnClickListener {
                if (type == ToastType.ERROR_RETRY) {
                    callRetry.invoke()
                }
                this.snackbar?.dismiss()
                snackbar.dismiss()
            }
            snackbar.show()
        }
    }

    private fun hideSnackbar() {
        snackbar?.dismiss()
    }
}