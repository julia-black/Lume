package com.singlelab.lume.ui.participants

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.base.OnlyForAuthFragments
import com.singlelab.lume.util.TextInputDebounce
import dagger.hilt.android.AndroidEntryPoint
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject


@AndroidEntryPoint
class ParticipantsFragment : BaseFragment(), ParticipantsView, OnlyForAuthFragments {

    @Inject
    lateinit var daggerPresenter: ParticipantsPresenter

    @InjectPresenter
    lateinit var presenter: ParticipantsPresenter

    @ProvidePresenter
    fun providePresenter() = daggerPresenter

    private var searchDebounce: TextInputDebounce? = null

    private var isSearchResults = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_participants, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            it.title = getString(R.string.title_my_friends)
        }
        arguments?.let {
//            val isSearch = FriendsFragmentArgs.fromBundle(it).isSearch
//            if (isSearch) {
//                edit_text_search.requestFocus()
//            }
//            presenter.eventUid = FriendsFragmentArgs.fromBundle(it).eventUid
        }
    }
}