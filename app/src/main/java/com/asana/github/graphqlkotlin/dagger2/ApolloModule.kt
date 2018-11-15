package com.asana.github.graphqlkotlin.dagger2

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.BuildConfig
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

/**
 * This class is a dagger Module which provides the Authentication interceptor, okHttpClient and Apollo client
 *
 * @author Gowsik K C
 * @version 1.0 ,11/02/2018
 * @since 1.0
 */

@Module
class ApolloModule {


    //Base url of the Git Hub graph Api
    private val baseUrl = "https://api.github.com/graphql"

    //access token
    private val accessToken = "c7f687c3269f9a9196c707d24f4ac223cbf60868"


    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
            HttpLoggingInterceptor()
                    .setLevel(
                            if (BuildConfig.DEBUG)
                                HttpLoggingInterceptor.Level.BODY
                            else
                                HttpLoggingInterceptor.Level.NONE)


    /**
     * This method constructs and returns the Auth interceptor
     * by adding the Authorization header with token to the request
     *
     * @return auth Interceptor okhttp
     */
    @Provides
    @Singleton
    fun provideInterceptor(): Interceptor = Interceptor { chain ->
        val original = chain.request()
        val builder = original.newBuilder()
                .method(original.method(), original.body())
                .header("Authorization", "Bearer " + accessToken)

        val request = builder.build()
        chain.proceed(request)
    }


    /**
     * This method builds the Okhttp client and adds the
     * authentication interceptor in it.
     *
     * @param authInterceptor which contains the auth header
     * @return okHttpClient
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor,
                            authInterceptor: Interceptor): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()


    /**
     * This method builds the Apollo client using the okhttp client and
     * the Base url
     *
     * @param okHttpClient okhttp3 client which contains the auth interceptor
     * @return apolloClient
     */
    @Provides
    @Singleton
    fun provideApolloClient(okHttpClient: OkHttpClient): ApolloClient = ApolloClient.builder()
            .serverUrl(baseUrl)
            .okHttpClient(okHttpClient)
            .build()



}
