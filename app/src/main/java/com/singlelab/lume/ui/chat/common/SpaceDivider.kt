package com.singlelab.lume.ui.chat.common

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.State

class SpaceDivider(
    private val space: Int
) : ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = space
        if (parent.getChildAdapterPosition(view) == 0) outRect.top = space else outRect.top = 0
    }
}