package com.asana.github.graphqlkotlin

import com.asana.github.graphqlkotlin.dagger2.DaggerGraphQlAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
/**
 * GraphQlApp  Application Class
 *
 *
 * @author Gowsik K C
 * @version 1.0 ,11/02/2018
 * @since 1.0
 */
class GraphQlApp : DaggerApplication() {

    /**
     * Indicates the main App Component and supplies the context
     */
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerGraphQlAppComponent.builder().application(this).build()
    }

}
