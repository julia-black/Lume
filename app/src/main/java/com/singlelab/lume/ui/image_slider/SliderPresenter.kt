package com.singlelab.lume.ui.image_slider

import android.app.DownloadManager
import android.net.Uri
import android.os.Environment
import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.model.Const
import com.singlelab.lume.pref.Preferences
import com.singlelab.lume.ui.image_slider.interactor.SliderInteractor
import com.singlelab.lume.util.generateImageLink
import com.singlelab.net.exceptions.ApiException
import moxy.InjectViewState
import java.io.File
import javax.inject.Inject

@InjectViewState
class SliderPresenter @Inject constructor(
    private val interactor: SliderInteractor,
    preferences: Preferences?
) : BasePresenter<SliderView>(preferences, interactor as BaseInteractor) {

    private var imageUids: MutableList<String>? = null

    private var eventUid: String? = null

    private var selectedPosition: Int = 0

    fun setImageUids(imageUids: Array<String>) {
        this.imageUids = imageUids.toMutableList()
        this.imageUids?.let { list ->
            viewState.showImages(list.map { it.generateImageLink() })
        }
    }

    fun setEventUid(eventUid: String?) {
        this.eventUid = eventUid
    }

    fun onSelectPosition(position: Int) {
        selectedPosition = position
        viewState.showOptionsButton(isShowDeleteButton = position > 0 && eventUid != null)
    }

    fun onClickDelete() {
        imageUids?.let { images ->
            val imageUid = images[selectedPosition]
            eventUid?.let {
                deleteImage(it, imageUid, selectedPosition)
            }
        }
    }

    private fun deleteImage(eventUid: String, imageUid: String, position: Int) {
        viewState.showLoading(isShow = true, withoutBackground = true)
        invokeSuspend {
            try {
                interactor.deleteImage(eventUid, imageUid)
                this.imageUids?.let { images ->
                    images.removeAll { it == imageUid }
                    runOnMainThread {
                        viewState.showSuccessDeleting(position)
                        viewState.showLoading(isShow = false, withoutBackground = true)
                    }
                }
            } catch (e: ApiException) {
                runOnMainThread {
                    viewState.showLoading(isShow = false, withoutBackground = true)
                    viewState.showError(e.message)
                }
            }
        }
    }

    fun onClickDownload() {
        imageUids?.let { images ->
            val imageUid = images[selectedPosition]
            val filename = "${imageUid}.jpg"
            val downloadUrlOfImage = imageUid.generateImageLink()
            val direct = File(
                Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    .absolutePath.toString() + "/" + Const.FOLDER_NAME + "/"
            )

            if (!direct.exists()) {
                direct.mkdir()
            }

            val downloadUri: Uri = Uri.parse(downloadUrlOfImage)
            val request = DownloadManager.Request(downloadUri)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(filename)
                .setMimeType("image/jpeg")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_PICTURES,
                    File.separator + Const.FOLDER_NAME + File.separator.toString() + filename
                )

            viewState.showStartDownload()
            viewState.saveImage(request)
        }
    }
}