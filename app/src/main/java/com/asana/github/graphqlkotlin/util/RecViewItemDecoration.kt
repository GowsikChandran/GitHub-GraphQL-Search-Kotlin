package com.asana.github.graphqlkotlin.util

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * RecViewItemDecoration is used to add space around the
 * recycler view item
 *
 * @author Gowsik K C
 * @version 1.0 ,11/02/2018
 * @since 1.0
 */

/**
 * Primary Constructor to receive the space value
 * @param space int value of the margin
 */
class RecViewItemDecoration(val space: Int) : RecyclerView.ItemDecoration() {


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        //applies top margin only for first item
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.set(space, space, space, space)
        } else {
            outRect.set(space, 0, space, space)

        }
    }
}