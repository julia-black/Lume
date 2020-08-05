package com.singlelab.lume.ui.view.person

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.singlelab.lume.model.profile.Person

class PersonAdapter(
    private val list: MutableList<Person>,
    private val eventUid: String? = null,
    private val isInviting: Boolean = false,
    private val listener: OnPersonItemClickListener
) : RecyclerView.Adapter<PersonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PersonViewHolder(
            inflater,
            parent
        )
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val person = list[position]
        holder.bind(person, eventUid, isInviting, listener)
    }

    override fun getItemCount(): Int = list.size

    fun addData(list: List<Person>) {
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun setData(list: List<Person>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
}