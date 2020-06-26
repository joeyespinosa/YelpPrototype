package com.axelia.yelpprototype.di.module

import com.axelia.yelpprototype.data.remote.api.YelpService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class ApiModule {

    @Singleton
    @Provides
    fun provideRetrofitService(): YelpService {
        val httpClient : OkHttpClient.Builder  = OkHttpClient.Builder()
        httpClient.addInterceptor(
            Interceptor {
                val original : Request = it.request()

                val request : Request = original.newBuilder()
                    .header("Authorization", "Bearer WszIxiroyARhF_Yc2CdwTkjmpn7121Q6vI3ydl6YHEkpB4kdD4zBkOLlTGjMSodltJY42EnAkwt9gPvGb2xuhtxApAVgHKIMaSEVi5A2jBNJcwdIE-4awGf3KKHwXnYx")
                    .method(original.method(), original.body())
                    .build();

                it.proceed(request)
            })

        val client : OkHttpClient = httpClient.build()

        return Retrofit.Builder()
            .baseUrl(YelpService.API_URL)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder()
                        .add(KotlinJsonAdapterFactory())
                        .build()
                )
            )
            .client(client)
            .build()
            .create(YelpService::class.java)
    }
}
