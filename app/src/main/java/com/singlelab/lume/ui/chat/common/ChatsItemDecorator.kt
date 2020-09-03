package com.singlelab.lume.ui.chat.common

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.singlelab.lume.util.dpToPx
import com.singlelab.lume.util.pxToDp


class ChatsItemDecorator(private val mDivider: Drawable) : ItemDecoration() {
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: State) {
        for (i in 0..parent.childCount - 2) {
            val child = parent.getChildAt(i)
            val dividerTop: Int = child.bottom + (child.layoutParams as LayoutParams).bottomMargin
            val dividerBottom = dividerTop + mDivider.intrinsicHeight
            mDivider.setBounds(parent.paddingLeft + 16.dpToPx().toInt(), dividerTop, parent.width - parent.paddingRight - 16.dpToPx().toInt(), dividerBottom)
            mDivider.draw(c)
        }
    }
}