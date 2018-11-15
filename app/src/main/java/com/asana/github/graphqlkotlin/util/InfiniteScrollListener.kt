package com.asana.github.graphqlkotlin.util

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

/**
 * InfiniteScrollListener adds a scroll listener when the
 * recycler view is scrolled
 *
 * @author Gowsik K C
 * @version 1.0 ,11/02/2018
 * @since 1.0
 */


abstract class InfiniteScrollListener : RecyclerView.OnScrollListener() {

    //new page start loading when two items are left
    private val VISIBLE_THRESHOLD = 2

    //to check the current loading state
    private var isLoading = false

    private var firstVisibleItem: Int = 0
    private var totalItemCount: Int = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)


        val visibleItemCount = recyclerView.childCount

        val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?

        if (linearLayoutManager != null) {

            totalItemCount = linearLayoutManager.itemCount
            firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition()
        }

        //when isLoading is false and the there are only two items
        //left in the list load more is called
        if (!isLoading && totalItemCount - visibleItemCount <= firstVisibleItem + VISIBLE_THRESHOLD) {

            isLoading = true
            onLoadMore()

        }


    }


    /**
     * This function is called when the end of the list is reached
     * to load more items
     */
    abstract fun onLoadMore()


    /**
     * Setter of the isloading Flag
     *
     * @param isLoading flat to indicate load more
     */
    fun setIsLoading(isLoading: Boolean) {
        this.isLoading = isLoading
    }
}
