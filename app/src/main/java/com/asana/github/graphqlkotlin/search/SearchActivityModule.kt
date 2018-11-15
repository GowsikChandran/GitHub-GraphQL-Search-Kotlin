package com.asana.github.graphqlkotlin.search

import android.os.Handler
import android.os.Looper
import android.support.v7.widget.LinearLayoutManager
import com.apollographql.apollo.ApolloClient
import com.asana.github.graphqlkotlin.R
import com.asana.github.graphqlkotlin.util.RecViewItemDecoration
import dagger.Binds
import dagger.Module
import dagger.Provides

/**
 * This is the Activity level Module for SearchActivity
 *
 * @author Gowsik K C
 * @version 1.0 ,11/02/2018
 * @since 1.0
 */
@Module
abstract class SearchActivityModule {

    //static function

    @Module
    companion object {


        /**
         * This function provides the Apps Main Thread
         *
         * @return handler
         */
        @Provides
        @JvmStatic
        fun provideMainHandler() = Handler(Looper.getMainLooper())


        /**
         * This function constructs and provides the SarchGraphQlRepository object
         *
         * @param apolloClient the apollo client to make requests
         * @param handler the handler to update the Ui
         */
        @Provides
        @JvmStatic
        fun provideRepository(apolloClient: ApolloClient, handler: Handler): SarchGraphQlRepository {
            return SarchGraphQlRepository(apolloClient, handler)
        }


        /**
         * This function constructs and provides the SearchPresenter object
         *
         * @param view the apollo client to make requests
         * @param repository the handler to update the Ui
         */
        @Provides
        @JvmStatic
        fun provideSearchPresenter(view: SearchViewContract, repository: SarchGraphQlRepository): SearchPresenter {
            return SearchPresenter(view, repository)
        }

        /**
         * This function constructs and provides the SearchRepoAdapter object
         *
         * @param searchActivity the search activity instance
         * @param SearchRepoAdapter adapter for recycler view
         */
        @Provides
        @JvmStatic
        fun provideSearchAdapter(searchActivity: SearchActivity): SearchRepoAdapter {
            return SearchRepoAdapter(searchActivity)
        }

        /**
         * This function constructs and provides the LinearLayoutManager object
         * to the recycler view
         *
         * @param searchActivity the search activity instance
         * @return LinearLayoutManager - recyclerView layout manager
         */
        @Provides
        @JvmStatic
        fun provideLayoutManager(searchActivity: SearchActivity): LinearLayoutManager {
            return LinearLayoutManager(searchActivity)
        }

        /**
         * This function constructs and provides the provideRvItemDecoration object
         *
         * @param searchActivity the search activity instance
         * @return RecViewItemDecoration object
         */
        @Provides
        @JvmStatic
        fun provideRvItemDecoration(searchActivity: SearchActivity): RecViewItemDecoration {
            return RecViewItemDecoration(searchActivity.resources.getDimensionPixelSize(R.dimen.search_rv_item_space))
        }
    }


    /**
     * This function returns the SearchViewContract
     *
     * @param searchActivity the search activity implements the Search view contract
     */
    @Binds
    abstract fun provideSearchView(searchActivity: SearchActivity): SearchViewContract




}
