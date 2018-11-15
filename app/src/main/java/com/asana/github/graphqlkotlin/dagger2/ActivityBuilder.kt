package com.asana.github.graphqlkotlin.dagger2

import com.asana.github.graphqlkotlin.search.SearchActivity
import com.asana.github.graphqlkotlin.search.SearchActivityModule

import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Provides Dagger with the activities that will inject
 * their respective modules
 *
 * @author Gowsik K C
 * @version 1.0 ,11/02/2018
 * @since 1.0
 */

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [(SearchActivityModule::class)])
    abstract fun bindSearchActivity(): SearchActivity


}
