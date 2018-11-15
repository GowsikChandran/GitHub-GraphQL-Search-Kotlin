package com.asana.github.graphqlkotlin.search

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import com.apollo.apolographql.api.SearchRepoQuery
import com.asana.github.graphqlkotlin.R
import com.asana.github.graphqlkotlin.util.InfiniteScrollListener
import com.asana.github.graphqlkotlin.util.RecViewItemDecoration
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_search.*
import javax.inject.Inject

/**
 * SearchActivity
 *
 * @author Gowsik K C
 * @version 1.0 ,11/02/2018
 * @since 1.0
 */
class SearchActivity : DaggerAppCompatActivity(), SearchViewContract {

    private val TAG = SearchActivity::class.simpleName

    //Dagger will initialise the values
    @Inject
    lateinit var searchPresenter: SearchPresenter

    @Inject
    lateinit var mAdapter: SearchRepoAdapter

    @Inject
    lateinit var mLinearLayoutManager: LinearLayoutManager

    @Inject
    lateinit var recViewItemDecoration: RecViewItemDecoration


    //after cursor should be null when loading first page
    var afterCursor: String? = null
    lateinit var query: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


        search_rv.layoutManager = mLinearLayoutManager

        search_rv.addItemDecoration(recViewItemDecoration)

        search_rv.adapter = mAdapter

        search_rv.addOnScrollListener(infiniteScrollListener)

        search_edt.setOnEditorActionListener(editorActionListener)

    }

    /**
     * OnEditorActionListener is triggered when the soft keyboard search
     * button is pressed
     */
    private val editorActionListener: TextView.OnEditorActionListener = TextView.OnEditorActionListener { view, actionId, _ ->

        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            if (!isNetworkAvailable()) {
                Toast.makeText(applicationContext, resources.getString(R.string.network_error_text), Toast.LENGTH_LONG).show()
                return@OnEditorActionListener true
            }
            //reset page cursor for new searches
            afterCursor = null

            //clears the any previous list in adapter
            mAdapter.clear()

            //sets is loading variable to true so that
            // on load more will not be called
            infiniteScrollListener.setIsLoading(true)

            query = search_edt.text.toString()


            getRepos(query, afterCursor)

            hideKeyboard(view)

            return@OnEditorActionListener true
        }
        false
    }


    /**
     * InfiniteScrollListener onLoad more method is triggered when end of the list
     * is reached
     */
    private val infiniteScrollListener: InfiniteScrollListener = object : InfiniteScrollListener() {

        override fun onLoadMore() {

            mAdapter.showLoading()

            getRepos(query, afterCursor)


        }
    }


    /**
     * getRepos method call the search Github Repos method
     * in the presenter
     *
     * @param query       the search keyword string
     * @param afterCursor the end cursor of the current page
     */
    internal fun getRepos(query: String, afterCursor: String?) {


        searchPresenter.searchGitHubRepos(query, afterCursor)

    }


    override fun displaySearchResults(repositoryList: List<SearchRepoQuery.Edge>, pageInfo: SearchRepoQuery.PageInfo) {


        //shows the main progress bar only on first load
        if (afterCursor != null) {
            mAdapter.hideLoading()
        }

        //adds the new set of results to the adapter list
        mAdapter.addRepos(repositoryList)

        //if the Repo result does not have a next page
        //there is no need to call on load more when the
        // end is reached
        if (pageInfo.hasNextPage()) {
            infiniteScrollListener.setIsLoading(false)

        } else {
            infiniteScrollListener.setIsLoading(true)
            displayError("No Items found")
        }
        //sets the latest end cursor
        afterCursor = pageInfo.endCursor()
    }

    override fun displayError() {
        //to hide progress bar if the loading fails on load more
        if (afterCursor != null) {
            mAdapter.hideLoading()
            infiniteScrollListener.setIsLoading(false)
        }

        Toast.makeText(applicationContext, resources.getString(R.string.error_text), Toast.LENGTH_LONG).show()
    }

    override fun displayError(errorMessage: String) {
        Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun setProgressBarVisibility(visibility: Int) {

        if (afterCursor == null) {

            image_list_pb.visibility = visibility
        }
    }


    /**
     * Hides the soft keyboard
     *
     * @param view - the edit text view form the OnEditorActionListener
     */
    private fun hideKeyboard(view: View) {

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * Check's the network Connectivity
     *
     * @return  Boolean status of the network connection
     */
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }
}
