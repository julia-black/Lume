package com.singlelab.lume.ui.image_slider

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.pref.Preferences
import com.singlelab.lume.ui.image_slider.interactor.SliderInteractor
import com.singlelab.lume.util.generateImageLink
import com.singlelab.net.exceptions.ApiException
import moxy.InjectViewState
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
    }
}