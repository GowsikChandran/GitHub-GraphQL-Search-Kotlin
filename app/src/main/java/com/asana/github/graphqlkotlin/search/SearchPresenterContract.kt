package com.asana.github.graphqlkotlin.search

/**
 * SearchPresenterContract interface for the presenter
 *
 *
 * @author Gowsik K C
 * @version 1.0 ,11/02/2018
 * @since 1.0
 */
interface SearchPresenterContract {

    /**
     * searches the git hub repository with keyword and after cursor
     *
     * @param searchQuery search keyword string
     * @param afterCursor after cursor string
     */
    fun searchGitHubRepos(searchQuery: String, afterCursor: String?)

}