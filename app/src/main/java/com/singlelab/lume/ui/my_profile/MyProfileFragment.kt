package com.singlelab.lume.ui.my_profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.singlelab.data.model.profile.Profile
import com.singlelab.lume.MainActivity
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.base.listeners.OnActivityResultListener
import com.singlelab.lume.base.listeners.OnToolbarListener
import com.singlelab.lume.util.generateImageLink
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_my_profile.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject


@AndroidEntryPoint
class MyProfileFragment : BaseFragment(), MyProfileView, OnToolbarListener,
    OnActivityResultListener {

    @Inject
    lateinit var daggerPresenter: MyProfilePresenter

    @InjectPresenter
    lateinit var presenter: MyProfilePresenter

    @ProvidePresenter
    fun provideMyProfilePresenter() = daggerPresenter

    private var navController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            it.title = getString(R.string.title_my_profile)
            navController = Navigation.findNavController(it, R.id.nav_host_fragment)
        }
        view.findViewById<ImageView>(R.id.image)
            .setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_my_profile_to_navigation_auth))
    }

    override fun onDestroy() {
        (activity as MainActivity?)?.showLogoutInToolbar(false)
        super.onDestroy()
    }

    override fun showProfile(profile: Profile) {
        (activity as MainActivity?)?.showLogoutInToolbar(true)
        name_age.text = "${profile.name}, ${profile.age}"
        description.text = profile.description
        if (!profile.imageContentUid.isNullOrEmpty()) {
            loadImage(profile.imageContentUid)
        } else {
            image.setImageDrawable(context?.getDrawable(R.drawable.ic_profile))
        }
        image.setOnClickListener {
            activity?.let { activity ->
                CropImage.activity()
                    .setFixAspectRatio(true)
                    .setRequestedSize(300, 300, CropImageView.RequestSizeOptions.RESIZE_FIT)
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .start(activity)
            }
        }

    }

    override fun navigateToAuth() {
        Navigation.createNavigateOnClickListener(R.id.action_navigation_my_profile_to_navigation_auth)
            .onClick(view)
    }

    override fun loadImage(imageUid: String?) {
        imageUid?.let {
            Glide.with(this)
                .load(imageUid.generateImageLink())
                .into(image)
        }
    }

    override fun onClickLogout() {
        navController?.let {
            presenter.logout()
        }
    }

    override fun onActivityResultFragment(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val bitmap =
                    MediaStore.Images.Media.getBitmap(activity?.contentResolver, result.uri)
                presenter.updateImageProfile(bitmap)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(context, getString(R.string.error_pick_image), Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}