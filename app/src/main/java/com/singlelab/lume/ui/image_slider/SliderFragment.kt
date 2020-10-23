package com.singlelab.lume.ui.image_slider


import android.app.DownloadManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.singlelab.lume.MainActivity
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.base.listeners.OnPermissionListener
import com.singlelab.lume.model.Const
import com.singlelab.lume.model.view.ToastType
import com.singlelab.lume.ui.view.image_slider.SliderAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_image_slider.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

@AndroidEntryPoint
class SliderFragment : BaseFragment(), SliderView, OnPermissionListener {

    @Inject
    lateinit var daggerPresenter: SliderPresenter

    @InjectPresenter
    lateinit var presenter: SliderPresenter

    @ProvidePresenter
    fun providePresenter() = daggerPresenter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_image_slider, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        showBottomNavigation(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            presenter.setImageUids(SliderFragmentArgs.fromBundle(it).imageUids)
            presenter.setEventUid(SliderFragmentArgs.fromBundle(it).eventUid)
        }
        showBottomNavigation(false)
        setListeners()
    }

    override fun showImages(links: List<String>) {
        view_pager.apply {
            adapter = SliderAdapter(links.toMutableList())
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    presenter.onSelectPosition(position)
                }
            })
        }
        if (links.size > 1) {
            TabLayoutMediator(tab_layout, view_pager) { _, _ -> }.attach()
        } else {
            tab_layout.isVisible = false
        }
    }

    override fun showOptionsButton(isShowDeleteButton: Boolean, isShowDownloadButton: Boolean) {
        button_delete.isVisible = isShowDeleteButton
        button_download.isVisible = isShowDownloadButton
    }

    override fun showSuccessDeleting(position: Int) {
        (view_pager.adapter as SliderAdapter).deleteItem(position)
        showSnackbar(getString(R.string.success_delete), ToastType.SUCCESS)
    }

    override fun saveImage(request: DownloadManager.Request) {
        context?.let {
            (it.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager).enqueue(request)
        }
    }

    override fun showStartDownload() {
        showSnackbar(getString(R.string.start_downloading, Const.FOLDER_NAME), ToastType.SUCCESS)
    }

    private fun setListeners() {
        button_back.setOnClickListener {
            findNavController().popBackStack()
        }
        button_delete.setOnClickListener {
            presenter.onClickDelete()
        }
        button_download.setOnClickListener {
            (activity as MainActivity?)?.checkWriteExternalPermission()
        }
    }

    override fun onLocationPermissionGranted() {
    }

    override fun onLocationPermissionDenied() {
    }

    override fun onContactsPermissionGranted() {
    }

    override fun onContactsPermissionDenied() {
    }

    override fun onWriteExternalPermissionGranted() {
        presenter.onClickDownload()
    }

    override fun onWriteExternalPermissionDenied() {
        showSnackbar(getString(R.string.access_denied))
    }
}