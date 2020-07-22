package com.singlelab.lume.ui.friends.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.singlelab.lume.model.profile.Person

class FriendsAdapter(
    private val list: List<Person>,
    private val listener: OnPersonItemClickListener
) : RecyclerView.Adapter<PersonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PersonViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val person = list[position]
        holder.bind(person, listener)
    }

    override fun getItemCount(): Int = list.size

}