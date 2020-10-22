package com.singlelab.lume.ui.view.pager

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.singlelab.lume.R
import com.singlelab.lume.model.profile.Person
import com.singlelab.lume.ui.view.pager.listener.OnFriendsClickListener
import com.singlelab.lume.ui.view.person_short.OnPersonShortClickListener
import com.singlelab.lume.ui.view.person_short.PersonShortAdapter
import kotlinx.android.synthetic.main.view_friends.view.*


class FriendsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), PagerTabView {

    private var clickListener: OnFriendsClickListener? = null

    private var personClickListener: OnPersonShortClickListener? = null

    private var friends: MutableList<Person> = mutableListOf()

    private var newFriends: MutableList<Person> = mutableListOf()

    init {
        LayoutInflater.from(context).inflate(R.layout.view_friends, this, true)
    }

    override fun getTitle() = context.getString(R.string.tab_friends)

    override fun getView() = this

    fun setFriendsListener(
        listener: OnFriendsClickListener,
        personClickListener: OnPersonShortClickListener
    ) {
        this.clickListener = listener
        this.personClickListener = personClickListener
        search_friends.setOnClickListener {
            clickListener?.onSearchFriendsClick()
        }
        show_all.setOnClickListener {
            clickListener?.onSearchFriendsClick()
        }
    }

    fun setFriends(friends: List<Person>?) {
        showLoading(false)
        search_friends.visibility = View.VISIBLE
        this.friends.clear()
        this.newFriends.clear()
        friends?.forEach {
            if (it.friendshipApprovalRequired) {
                this.newFriends.add(it)
            } else {
                if (this.friends.size < 4) {
                    this.friends.add(it)
                }
            }
        }
        if (this.friends.isNullOrEmpty()) {
            recycler_friends.visibility = View.GONE
            title_empty_friends.visibility = View.VISIBLE
            title_invite_friends.visibility = View.VISIBLE
            if (newFriends.isNullOrEmpty()) {
                show_all.visibility = View.GONE
            } else {
                show_all.visibility = View.VISIBLE
                show_all.text = context.getString(R.string.new_friends, newFriends.size)
            }
        } else {
            title_empty_friends.visibility = View.GONE
            title_invite_friends.visibility = View.GONE
            recycler_friends.visibility = View.VISIBLE
            recycler_friends.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                visibility = View.VISIBLE
                adapter = PersonShortAdapter(this@FriendsView.friends, personClickListener)
            }
            show_all.visibility = View.VISIBLE
            val showAllText = context.getString(R.string.show_all, friends?.size)
            if (newFriends.isNullOrEmpty()) {
                show_all.text = showAllText
            } else {
                val text = "$showAllText +${newFriends.size}"
                val spannable = SpannableString(text)
                spannable.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorNotification)),
                    showAllText.length + 1, text.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                show_all.text = spannable
            }
        }
        title_invite_friends.setOnClickListener {
            clickListener?.onInviteFriendsClick()
        }
    }

    fun showLoading(isShow: Boolean) {
        loading.isVisible = isShow
        recycler_friends.isVisible = !isShow
        show_all.isVisible = !isShow
        search_friends.isVisible = !isShow
    }
}