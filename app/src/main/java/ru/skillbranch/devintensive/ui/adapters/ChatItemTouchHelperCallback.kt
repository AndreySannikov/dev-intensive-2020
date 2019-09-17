package ru.skillbranch.devintensive.ui.adapters

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.data.ChatItem

class ChatItemTouchHelperCallback(
    private val adapter: ChatAdapter,
    private val swipeIcon: Drawable,
    private val swipeListener: (ChatItem) -> Unit
) :
    ItemTouchHelper.Callback() {

    private val bgRect = RectF()
    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val iconBounds = Rect()

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return if (viewHolder is ItemTouchViewHolder) {
            makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE, ItemTouchHelper.START)
        } else {
            makeFlag(ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.START)
        }
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        swipeListener.invoke(adapter.items[viewHolder.adapterPosition])
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE && viewHolder is ItemTouchViewHolder) {
            viewHolder.onItemSelected()
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        if (viewHolder is ItemTouchViewHolder) {
            viewHolder.onItemCleared()
        }
        super.clearView(recyclerView, viewHolder)
    }

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            with(viewHolder.itemView) {
                drawBackground(canvas, this, dX)
                drawIcon(canvas, this, dX)
            }
        }
        super.onChildDraw(
            canvas,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
    }

    private fun drawBackground(canvas: Canvas, view: View, dX: Float) {
        with(bgRect) {
            left = view.right.toFloat() + dX
            top = view.top.toFloat()
            right = view.right.toFloat()
            bottom = view.bottom.toFloat()
        }

        with(bgPaint) {
            color = TypedValue().apply {
                view.context.theme.resolveAttribute(R.attr.colorSwipeBackground, this, true)
            }.data
        }

        canvas.drawRect(bgRect, bgPaint)
    }

    private fun drawIcon(canvas: Canvas, view: View, dX: Float) {
        val iconSize = view.resources.getDimensionPixelSize(R.dimen.icon_size)
        val space = view.resources.getDimensionPixelSize(R.dimen.spacing_normal_16)

        val margin = (view.bottom - view.top - iconSize) / 2

        with(iconBounds) {
            left = view.right + dX.toInt() + space
            top = view.top + margin
            right = left + iconSize
            bottom = view.bottom - margin
        }

        swipeIcon.bounds = iconBounds
        swipeIcon.draw(canvas)
    }
}

interface ItemTouchViewHolder {
    fun onItemSelected()
    fun onItemCleared()
}