package com.singlelab.lume.ui.view.person_short

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.singlelab.lume.model.profile.Person

class PersonShortAdapter(
    private val list: List<Person>,
    private val listener: OnPersonShortClickListener?
) : RecyclerView.Adapter<PersonShortViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonShortViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PersonShortViewHolder(
            inflater,
            parent
        )
    }

    override fun onBindViewHolder(holder: PersonShortViewHolder, position: Int) {
        val person = list[position]
        holder.bind(person, listener)
    }

    override fun getItemCount(): Int = list.size

}