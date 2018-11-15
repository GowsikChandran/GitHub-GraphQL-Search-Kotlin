package com.asana.github.graphqlkotlin.dagger2

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module


/**
 * Application level module witch provides dagger with the context
 *
 * @author Gowsik K C
 * @version 1.0 ,11/02/2018
 * @since 1.0
 */

@Module
abstract class AppModule {

    /**
     * Provides Dagger with the Application level context
     *
     * @param application application context
     * @return returns the context
     */

    @Binds
    abstract fun providesContext(application: Application): Context


}
