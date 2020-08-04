package com.singlelab.lume.ui.view.pager

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.singlelab.lume.R
import com.singlelab.lume.model.profile.Person
import com.singlelab.lume.ui.view.image_person.ImagePersonAdapter
import com.singlelab.lume.ui.view.image_person.OnPersonImageClickListener
import com.singlelab.lume.ui.view.pager.listener.OnFriendsClickListener
import kotlinx.android.synthetic.main.view_friends.view.*


class FriendsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var clickListener: OnFriendsClickListener? = null

    private var personClickListener: OnPersonImageClickListener? = null

    private var friends: List<Person>? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_friends, this, true)
    }

    fun setFriendsListener(
        listener: OnFriendsClickListener,
        personImageClickListener: OnPersonImageClickListener
    ) {
        this.clickListener = listener
        this.personClickListener = personImageClickListener
    }

    fun setFriends(friends: List<Person>) {
        this.friends = friends
        if (friends.isEmpty()) {
            recycler_friends.visibility = View.GONE
        } else {
            recycler_friends.visibility = View.VISIBLE
        }
        search_friends.setOnClickListener {
            clickListener?.onSearchFriendsClick()
        }
        recycler_friends.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            visibility = View.VISIBLE
            adapter = ImagePersonAdapter(
                friends,
                personClickListener
            )
        }
    }
}