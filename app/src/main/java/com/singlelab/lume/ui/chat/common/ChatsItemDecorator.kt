package com.singlelab.lume.ui.chat.common

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.singlelab.lume.util.dpToPx

class ChatsItemDecorator(private val mDivider: Drawable) : ItemDecoration() {
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: State) {
        for (i in 0..parent.childCount - 2) {
            val dividerLeft = parent.paddingLeft + 16.dpToPx().toInt()
            val dividerTop: Int = parent.getChildAt(i).bottom + (parent.getChildAt(i).layoutParams as LayoutParams).bottomMargin
            val dividerRight = parent.width - parent.paddingRight - 16.dpToPx().toInt()
            val dividerBottom = dividerTop + mDivider.intrinsicHeight
            mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
            mDivider.draw(c)
        }
    }
}