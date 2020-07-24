package com.singlelab.lume.ui.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.base.OnlyForAuthFragments
import com.singlelab.lume.model.profile.Person
import com.singlelab.lume.ui.friends.adapter.OnPersonItemClickListener
import com.singlelab.lume.ui.friends.adapter.PersonsAdapter
import com.singlelab.lume.util.TextInputDebounce
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

    private var searchDebounce: TextInputDebounce? = null

    private var isSearchResults = false

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
            presenter.eventUid = FriendsFragmentArgs.fromBundle(it).eventUid
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
        title_empty_search.visibility = View.GONE
        if (searchDebounce == null) {
            searchDebounce = TextInputDebounce(
                editText = edit_text_search,
                isHandleEmptyString = true
            )
            searchDebounce!!.watch {
                presenter.search(it)
            }
        }
    }

    override fun showFriends(friends: List<Person>?) {
        isSearchResults = false
        title_empty_search.visibility = View.GONE
        recycler_search_results.visibility = View.GONE
        if (friends.isNullOrEmpty()) {
            showEmptyFriends()
        } else {
            title_empty_friends.visibility = View.GONE
            recycler_friends.visibility = View.VISIBLE
            recycler_friends.adapter = PersonsAdapter(friends, presenter.eventUid, this)
        }
    }

    override fun showSearchResult(searchResults: List<Person>?) {
        isSearchResults = true
        recycler_friends.visibility = View.GONE
        title_empty_friends.visibility = View.GONE
        if (searchResults.isNullOrEmpty()) {
            title_empty_search.visibility = View.VISIBLE
            recycler_search_results.visibility = View.GONE
        } else {
            title_empty_search.visibility = View.GONE
            recycler_search_results.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                visibility = View.VISIBLE
                adapter = PersonsAdapter(searchResults, presenter.eventUid, this@FriendsFragment)
            }
        }
    }

    override fun onPersonClick(personUid: String) {
        findNavController().navigate(FriendsFragmentDirections.actionFriendsToPerson(personUid))
    }

    override fun onChatClick(personUid: String) {
        //todo переход на чат
    }

    override fun onAddToFriends(personUid: String) {
        presenter.addToFriends(personUid)
    }

    override fun onInviteClick(personUid: String, eventUid: String) {
        presenter.invitePerson(personUid, eventUid, isSearchResults)
    }

    private fun showEmptyFriends() {
        recycler_friends.visibility = View.GONE
        title_empty_friends.visibility = View.VISIBLE
    }

    private fun hasFriends(): Boolean {
        return true
    }
}