package com.asana.github.graphqlkotlin

import android.view.View
import com.apollo.apolographql.api.SearchRepoQuery
import com.apollographql.apollo.api.Response
import com.asana.github.graphqlkotlin.search.SarchGraphQlRepository
import com.asana.github.graphqlkotlin.search.SearchPresenter
import com.asana.github.graphqlkotlin.search.SearchViewContract
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.lang.Exception
import java.util.*

class SearchPresenterTest {

    private var presenter: SearchPresenter? = null

    @Mock
    private val repository: SarchGraphQlRepository? = null
    @Mock
    private val viewContract: SearchViewContract? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        // for the "@Mock" annotations
        MockitoAnnotations.initMocks(this)

        // Make presenter a mock while using mock repository and viewContract created above
        presenter = Mockito.spy(SearchPresenter(viewContract!!, repository!!))

    }

    @Test
    fun searchGitHubRepos_noQuery() {
        val searchQuery: String = ""
        val afterCursor: String? = null

        // Trigger
        presenter!!.searchGitHubRepos(searchQuery, afterCursor)

        // Validation
        Mockito.verify<SarchGraphQlRepository>(repository, Mockito.never()).searchQuery(searchQuery, afterCursor, presenter!!)
        Mockito.verify<SearchViewContract>(viewContract, Mockito.never()).setProgressBarVisibility(1)

        Mockito.verify<SearchViewContract>(viewContract, Mockito.times(1)).displayError("Please Enter a keyword")
    }

    @Test
    fun searchGitHubRepos() {
        val searchQuery = "keyword"
        val afterCursor: String? = null

        // Trigger
        presenter!!.searchGitHubRepos(searchQuery, afterCursor)

        // Validation
        Mockito.verify<SarchGraphQlRepository>(repository, Mockito.times(1)).searchQuery(searchQuery, afterCursor, presenter!!)
        Mockito.verify<SearchViewContract>(viewContract, Mockito.times(1)).setProgressBarVisibility(0)

        Mockito.verify<SearchViewContract>(viewContract, Mockito.never()).displayError("Please Enter a keyword")

    }

    @Test
    fun handleGitHubResponse_Success() {

        val response  = Mockito.mock(Response::class.java)

        val data = Mockito.mock<SearchRepoQuery.Data>(SearchRepoQuery.Data::class.java)

        val search = Mockito.mock<SearchRepoQuery.Search>(SearchRepoQuery.Search::class.java)

        val edge = Mockito.mock<SearchRepoQuery.Edge>(SearchRepoQuery.Edge::class.java)

        val expectedPageInfo = Mockito.mock<SearchRepoQuery.PageInfo>(SearchRepoQuery.PageInfo::class.java)


        Mockito.doReturn(false).`when`(response).hasErrors()

        Mockito.doReturn(data).`when`(response).data()

        val mockedList = Arrays.asList<SearchRepoQuery.Edge>(edge, edge, edge)


        Mockito.doReturn(search).`when`(data).search()

        Mockito.`when`(data.search().edges()).thenReturn(mockedList)
        Mockito.`when`(data.search().pageInfo()).thenReturn(expectedPageInfo)

        // Trigger
        presenter!!.handleGitHubResponse(response as Response<SearchRepoQuery.Data>)

        // Validation
        Mockito.verify<SearchViewContract>(viewContract, Mockito.times(1)).displaySearchResults(
                mockedList, expectedPageInfo)
        Mockito.verify<SearchViewContract>(viewContract, Mockito.times(1)).setProgressBarVisibility(View.GONE)

    }

    @Test
    fun handleGitHubResponse_Failure() {

        val response  = Mockito.mock(Response::class.java)

        Mockito.doReturn(true).`when`(response).hasErrors()

        // Trigger
        presenter!!.handleGitHubResponse(response as Response<SearchRepoQuery.Data>)

        // Validation
        Mockito.verify<SearchViewContract>(viewContract, Mockito.times(1)).displayError()
        Mockito.verify<SearchViewContract>(viewContract, Mockito.times(1)).setProgressBarVisibility(View.GONE)
    }

    @Test
    fun handleGitHubResponse_EmptyResponse() {
        val response  = Mockito.mock(Response::class.java)

        Mockito.doReturn(true).`when`(response).hasErrors()

        // Trigger
        presenter?.handleGitHubResponse(response as Response<SearchRepoQuery.Data>)

        // Validation
        Mockito.verify<SearchViewContract>(viewContract, Mockito.times(1)).displayError()
    }

    @Test
    fun handleGitHubError() {
        // Trigger
        presenter!!.handleGitHubError()

        // Validation
        Mockito.verify<SearchViewContract>(viewContract, Mockito.times(1)).setProgressBarVisibility(View.GONE)
        Mockito.verify<SearchViewContract>(viewContract, Mockito.times(1)).displayError()
    }
}