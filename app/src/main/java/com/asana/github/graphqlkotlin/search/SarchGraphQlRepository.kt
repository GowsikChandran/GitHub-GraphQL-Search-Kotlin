package com.asana.github.graphqlkotlin.search

import android.os.Handler
import android.util.Log
import com.apollo.apolographql.api.SearchRepoQuery
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloCallback
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloQueryCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.fetcher.ApolloResponseFetchers

import javax.inject.Inject


/**
 * SarchGraphQlRepository class is where the actual call to the
 * Git Hub GraphQL is made
 *
 * @author Gowsik K C
 * @version 1.0 ,11/02/2018
 * @since 1.0
 */

class SarchGraphQlRepository
@Inject
constructor( val apolloClient: ApolloClient, val handler: Handler) {

    private val TAG = SarchGraphQlRepository::class.simpleName

    fun searchQuery(query: String, afterCursor: String?,
                    callback: GitGraphQlRepositoryCallback) {


        //number of repositories per request
        val pageLimit = 20

        //builds the search query
        val searchRepoQuery = SearchRepoQuery.builder()
                .query(query)
                .limit(pageLimit.toLong())
                .afterCursor(afterCursor)
                .build()

        val searchApolloCall = apolloClient.query(searchRepoQuery).responseFetcher(ApolloResponseFetchers.NETWORK_ONLY)


        (searchApolloCall as ApolloQueryCall<SearchRepoQuery.Data>)
                .enqueue(ApolloCallback(object : ApolloCall.Callback<SearchRepoQuery.Data>() {


                    override fun onResponse(response: Response<SearchRepoQuery.Data>) {


                        callback.handleGitHubResponse(response)

                    }

                    override fun onFailure(e: ApolloException) {

                        callback.handleGitHubError()
                    }
                }, handler))

    }


    /**
     * Callback Interface for the presenter the handle the success and failure
     *
     * @author Gowsik K C
     * @version 1.0 ,11/02/2018
     * @since 1.0
     */
    interface GitGraphQlRepositoryCallback {

        /**
         * Handles the success response from the Apollo call
         *
         * @param response the response from the apollo call
         */
        fun handleGitHubResponse(response: Response<SearchRepoQuery.Data>)


        /**
         * Handles the Failure response from the Apollo call
         */
        fun handleGitHubError()

    }

}