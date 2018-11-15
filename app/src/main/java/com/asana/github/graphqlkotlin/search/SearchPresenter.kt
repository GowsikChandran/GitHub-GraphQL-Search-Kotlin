package com.asana.github.graphqlkotlin.search

import android.util.Log
import android.view.View
import com.apollo.apolographql.api.SearchRepoQuery
import com.apollographql.apollo.api.Response
import javax.inject.Inject

/**
 * SearchPresenter implements the SearchPresenterContract to pass the
 * query and after cursor to the repository
 * And also implements the GitGraphQlRepositoryCallback to handle the
 * success and failure response from the SarchGraphQlRepository and
 * update the view through the view contract
 *
 * @author Gowsik K C
 * @version 1.0 ,11/02/2018
 * @since 1.0
 */

class SearchPresenter
@Inject
constructor(val viewContract: SearchViewContract, val repository: SarchGraphQlRepository) :
        SearchPresenterContract, SarchGraphQlRepository.GitGraphQlRepositoryCallback {

    private val TAG = SearchPresenter::class.simpleName

    override fun searchGitHubRepos(searchQuery: String, afterCursor: String?) {

        if (searchQuery.length > 0) {

            viewContract.setProgressBarVisibility(View.VISIBLE)
            repository.searchQuery(searchQuery, afterCursor, this)

        } else {
            viewContract.displayError("Please Enter a keyword")
        }
    }

    override fun handleGitHubResponse(response: Response<SearchRepoQuery.Data>) {
        viewContract.setProgressBarVisibility(View.GONE)

        if (!response.hasErrors()) {

            val repoList = response.data()!!.search().edges()
            val pageInfo = response.data()!!.search().pageInfo()

            viewContract.displaySearchResults(repoList!!, pageInfo)

        } else {

            viewContract.displayError()
        }
    }

    override fun handleGitHubError() {
        viewContract.setProgressBarVisibility(View.GONE)
        viewContract.displayError()
    }

}
