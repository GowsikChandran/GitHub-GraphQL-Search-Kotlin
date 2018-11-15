package com.asana.github.graphqlkotlin.search

import com.apollo.apolographql.api.SearchRepoQuery

/**
 * SearchViewContract interface for activity
 *
 *
 * @author Gowsik K C
 * @version 1.0 ,11/02/2018
 * @since 1.0
 */
interface SearchViewContract {


    /**
     * Display the results
     * @param repositoryList a list of git hub repositories
     * @param pageInfo contains the after cursor and is last page variable
     */
    fun displaySearchResults(repositoryList: List<SearchRepoQuery.Edge>,
                             pageInfo: SearchRepoQuery.PageInfo)


    /**
     * Displays a standard error message
     */
    fun displayError()


    /**
     * Displays a custom error message
     * @param errorMessage custom string error message
     */
    fun displayError(errorMessage: String)


    /**
     * Sets the visibility if the main progress bar
     * @param visibility the visibility values are View.VISIBLE or View.GONE
     */
    fun setProgressBarVisibility(visibility: Int)


}
