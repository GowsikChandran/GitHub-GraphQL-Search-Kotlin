package com.asana.github.graphqlkotlin.dagger2

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * This is the Application level Component
 *
 *
 * @author Gowsik K C
 * @version 1.0 ,11/02/2018
 * @since 1.0
 */

@PerActivity
@Singleton
@Component(modules = [
    (AndroidSupportInjectionModule::class),
    (ApolloModule::class),
    (ActivityBuilder::class)])
interface GraphQlAppComponent : AndroidInjector<DaggerApplication> {

    override fun inject(instance: DaggerApplication?)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): GraphQlAppComponent

    }

}
