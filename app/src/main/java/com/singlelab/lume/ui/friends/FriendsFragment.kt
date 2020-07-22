package com.singlelab.lume.ui.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.singlelab.data.model.profile.Person
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.base.OnlyForAuthFragments
import com.singlelab.lume.ui.friends.adapter.FriendsAdapter
import com.singlelab.lume.ui.friends.adapter.OnPersonItemClickListener
import com.singlelab.lume.ui.my_profile.MyProfileFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_friends.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject


@AndroidEntryPoint
class FriendsFragment : BaseFragment(), FriendsView, OnlyForAuthFragments,
    OnPersonItemClickListener {

    @Inject
    lateinit var daggerPresenter: FriendsPresenter

    @InjectPresenter
    lateinit var presenter: FriendsPresenter

    @ProvidePresenter
    fun providePresenter() = daggerPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_friends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            it.title = getString(R.string.title_my_friends)
        }
        arguments?.let {
            val isSearch = FriendsFragmentArgs.fromBundle(it).isSearch
            if (isSearch) {
                edit_text_search.requestFocus()
            }
        }
        if (hasFriends()) {
            recycler_friends.apply {
                layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                visibility = View.VISIBLE
            }
            presenter.loadFriends()
            title_empty_friends.visibility = View.GONE
        } else {
            showEmptyFriends()
        }
    }

    override fun showFriends(friends: List<Person>?) {
        if (friends.isNullOrEmpty()) {
            showEmptyFriends()
        } else {
            recycler_friends.adapter = FriendsAdapter(friends, this)
        }
    }

    override fun onPersonClick(personUid: String) {
        findNavController().navigate(FriendsFragmentDirections.actionFriendsToPerson(personUid))
    }

    override fun onChatClick(personUid: String) {
        //todo переход на чат
    }

    private fun showEmptyFriends() {
        recycler_friends.visibility = View.GONE
        title_empty_friends.visibility = View.VISIBLE
    }

    private fun hasFriends(): Boolean {
        return true
    }
}