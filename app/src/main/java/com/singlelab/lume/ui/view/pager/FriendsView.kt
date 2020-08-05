package com.singlelab.lume.ui.view.pager

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.singlelab.lume.R
import com.singlelab.lume.model.profile.Person
import com.singlelab.lume.ui.view.pager.listener.OnFriendsClickListener
import com.singlelab.lume.ui.view.person_short.OnPersonShortClickListener
import com.singlelab.lume.ui.view.person_short.PersonShortAdapter
import kotlinx.android.synthetic.main.view_friends.view.*


class FriendsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var clickListener: OnFriendsClickListener? = null

    private var personClickListener: OnPersonShortClickListener? = null

    private var friends: List<Person>? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_friends, this, true)
    }

    fun setFriendsListener(
        listener: OnFriendsClickListener,
        personClickListener: OnPersonShortClickListener
    ) {
        this.clickListener = listener
        this.personClickListener = personClickListener
        search_friends.setOnClickListener {
            clickListener?.onSearchFriendsClick()
        }
    }

    fun setFriends(friends: List<Person>?) {
        this.friends = friends
        if (friends.isNullOrEmpty()) {
            recycler_friends.visibility = View.GONE
        } else {
            recycler_friends.visibility = View.VISIBLE
            recycler_friends.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                visibility = View.VISIBLE
                adapter = PersonShortAdapter(friends, personClickListener)
            }
        }
    }
}