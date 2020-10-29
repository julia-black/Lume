package com.singlelab.lume.ui.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.singlelab.lume.MainActivity
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.base.OnlyForAuthFragments
import com.singlelab.lume.base.listeners.OnPermissionListener
import com.singlelab.lume.model.Const
import com.singlelab.lume.model.profile.Person
import com.singlelab.lume.ui.chat.common.ChatOpeningInvocationType
import com.singlelab.lume.ui.view.person.OnPersonItemClickListener
import com.singlelab.lume.ui.view.person.PersonAdapter
import com.singlelab.lume.util.ContactsUtil
import com.singlelab.lume.util.TextInputDebounce
import com.singlelab.lume.util.toShortPhone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_friends.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject


@AndroidEntryPoint
class FriendsFragment : BaseFragment(), FriendsView, OnlyForAuthFragments,
    OnPersonItemClickListener, OnPermissionListener {

    @Inject
    lateinit var daggerPresenter: FriendsPresenter

    @InjectPresenter
    lateinit var presenter: FriendsPresenter

    @ProvidePresenter
    fun providePresenter() = daggerPresenter

    private var searchDebounce: TextInputDebounce? = null

    private var isSearchResults = false

    private var isContacts = false

    private var searchAdapter: PersonAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_friends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val isSearch = FriendsFragmentArgs.fromBundle(it).isSearch
            if (isSearch) {
                edit_text_search.requestFocus()
            }
            presenter.eventUid = FriendsFragmentArgs.fromBundle(it).eventUid
            presenter.participantIds = FriendsFragmentArgs.fromBundle(it).participantIds
            showSearch(presenter.eventUid.isNullOrEmpty())
        }
        recycler_friends.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            visibility = View.VISIBLE
        }
        edit_text_search.apply {
            setSingleLine()
            setHint(getString(R.string.search_friends))
            setStartDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_search))
        }
        searchDebounce = TextInputDebounce(
            editText = edit_text_search.getEditText(),
            isHandleEmptyString = true
        )
        searchDebounce!!.watch {
            if (isContacts && it.isEmpty()) {
                return@watch
            } else {
                isContacts = false
                presenter.search(it)
            }
        }
        button_import_contacts.setOnClickListener {
            onImportContactsClick()
        }
        title_invite_friends.setOnClickListener {
            shareText(Const.STORE_URL)
        }
    }

    override fun showFriends(friends: MutableList<Person>?) {
        isSearchResults = false
        recycler_search_results.visibility = View.GONE
        title_empty_search.visibility = View.GONE
        if (friends.isNullOrEmpty()) {
            showEmptyFriends()
        } else {
            title_empty_friends.visibility = View.GONE
            title_invite_friends.visibility = View.GONE
            recycler_friends.visibility = View.VISIBLE
            recycler_friends.adapter =
                PersonAdapter(
                    list = friends,
                    eventUid = presenter.eventUid,
                    participantIds = presenter.participantIds,
                    isInviting = true,
                    isAdministrator = false,
                    listener = this
                )
        }
    }

    override fun showSearchResult(searchResults: MutableList<Person>, page: Int) {
        isSearchResults = true
        recycler_friends.visibility = View.GONE
        title_empty_friends.visibility = View.GONE
        title_invite_friends.visibility = View.GONE
        title_empty_search.visibility = View.GONE
        if (searchResults.isNullOrEmpty() && page == 1) {
            title_empty_search.visibility = View.VISIBLE
            recycler_search_results.visibility = View.GONE
        } else if (searchResults.isNotEmpty()) {
            title_empty_search.visibility = View.GONE
            recycler_search_results.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                visibility = View.VISIBLE
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        if (layoutManager != null) {
                            val layoutManager = layoutManager as LinearLayoutManager
                            if (dy > 0 && !presenter.isLoading &&
                                (layoutManager.childCount + layoutManager.findFirstVisibleItemPosition()) >= layoutManager.itemCount
                            ) {
                                presenter.search(
                                    edit_text_search.getText(),
                                    ++presenter.pageNumber
                                )
                            }
                        }
                    }
                })
            }
            if (searchAdapter == null || page == 1) {
                searchAdapter = PersonAdapter(
                    list = searchResults,
                    eventUid = presenter.eventUid,
                    participantIds = presenter.participantIds,
                    isInviting = true,
                    isAdministrator = false,
                    listener = this@FriendsFragment
                )
                recycler_search_results.adapter = searchAdapter
            } else {
                (recycler_search_results.adapter as PersonAdapter?)?.addData(searchResults)
            }
        }
    }

    override fun showContacts(persons: MutableList<Person>) {
        showSearchResult(persons, 1)
        isContacts = true
        edit_text_search.setText("")
    }

    override fun showEmptyFriends() {
        recycler_search_results.visibility = View.GONE
        title_empty_search.visibility = View.GONE
        recycler_friends.visibility = View.GONE
        title_empty_friends.visibility = View.VISIBLE
        title_invite_friends.visibility = View.VISIBLE
    }

    override fun showEmptyPersonsFromContacts() {
        showSnackbar(getString(R.string.empty_persons_from_contacts))
    }

    override fun onPersonClick(personUid: String) {
        findNavController().navigate(FriendsFragmentDirections.actionFriendsToPerson(personUid))
    }

    override fun onChatClick(personName: String, personUid: String) {
        findNavController().navigate(
            FriendsFragmentDirections.actionFromFriendsToChat(
                null,
                ChatOpeningInvocationType.Person(
                    title = personName,
                    personUid = personUid
                )
            )
        )
    }

    override fun onAddToFriends(personUid: String) {
        presenter.addToFriends(personUid)
    }

    override fun onAcceptClick(personUid: String, eventUid: String) {
        presenter.invitePerson(personUid, eventUid, isSearchResults)
    }

    override fun onRejectClick(personUid: String, eventUid: String) {
    }

    override fun onRemoveFriendClick(personUid: String) {
        presenter.removeFriend(personUid, isSearchResults)
    }

    override fun onConfirmFriendClick(personUid: String) {
        presenter.confirmFriend(personUid, isSearchResults)
    }

    private fun showSearch(isShow: Boolean) {
        button_import_contacts.visibility = if (isShow) View.VISIBLE else View.GONE
        edit_text_search.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    private fun onImportContactsClick() {
        (activity as MainActivity).checkContactsPermission()
    }

    override fun onLocationPermissionGranted() {
    }

    override fun onLocationPermissionDenied() {
    }

    override fun onContactsPermissionGranted() {
        activity?.contentResolver?.let {
            val contacts = ContactsUtil.getContacts(it)
            presenter.loadPersonsFromContacts(contacts.map { contact ->
                contact.phone.toShortPhone()
            })
        }
    }

    override fun onContactsPermissionDenied() {
        showSnackbar(getString(R.string.not_permission_contacts))
    }

    override fun onWriteExternalPermissionGranted() {
    }

    override fun onWriteExternalPermissionDenied() {
    }
}