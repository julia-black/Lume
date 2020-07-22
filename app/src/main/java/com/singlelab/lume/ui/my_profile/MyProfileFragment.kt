package com.singlelab.lume.ui.my_profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.singlelab.data.model.profile.Profile
import com.singlelab.lume.MainActivity
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.base.listeners.OnActivityResultListener
import com.singlelab.lume.base.listeners.OnToolbarListener
import com.singlelab.lume.ui.my_profile.adapter.ImagePersonAdapter
import com.singlelab.lume.ui.my_profile.adapter.OnPersonImageClickListener
import com.singlelab.lume.util.generateImageLink
import com.singlelab.lume.util.getBitmap
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_my_profile.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject


@AndroidEntryPoint
class MyProfileFragment : BaseFragment(), MyProfileView, OnToolbarListener,
    OnActivityResultListener, OnPersonImageClickListener {

    @Inject
    lateinit var daggerPresenter: MyProfilePresenter

    @InjectPresenter
    lateinit var presenter: MyProfilePresenter

    @ProvidePresenter
    fun provideMyProfilePresenter() = daggerPresenter

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
        }
        view.findViewById<ImageView>(R.id.image)
            .setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_my_profile_to_auth))
    }

    override fun onStop() {
        (activity as MainActivity?)?.showLogoutInToolbar(false)
        super.onStop()
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
        if (profile.friends.isEmpty()) {
            search_friends.visibility = View.VISIBLE
            search_friends.setOnClickListener {
                toFriends(true)
            }
            recycler_friends.visibility = View.GONE
        } else {
            search_friends.visibility = View.GONE
            recycler_friends.apply {
                layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                visibility = View.VISIBLE
                adapter = ImagePersonAdapter(profile.friends, this@MyProfileFragment)
            }
        }
        title_friends.setOnClickListener {
            toFriends()
        }
    }

    override fun navigateToAuth() {
        Navigation.createNavigateOnClickListener(R.id.action_my_profile_to_auth)
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
        presenter.logout()
    }

    override fun onActivityResultFragment(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val bitmap = result.uri.getBitmap(activity?.contentResolver)
                presenter.updateImageProfile(bitmap)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(context, getString(R.string.error_pick_image), Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun onPersonClick(personUid: String) {
        //todo переход на профиль юзера
    }

    private fun toFriends(isSearch: Boolean = false) {
        val action = MyProfileFragmentDirections.actionMyProfileToFriends()
        action.isSearch = isSearch
        findNavController().navigate(action)
    }
}