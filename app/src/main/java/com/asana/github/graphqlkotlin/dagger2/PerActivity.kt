package com.asana.github.graphqlkotlin.dagger2


import javax.inject.Scope

/**
 * Creates a custom annotation to define scope
 *
 * @author Gowsik K C
 * @version 1.0 ,11/02/2018
 * @since 1.0
 */

@MustBeDocumented
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class PerActivity
