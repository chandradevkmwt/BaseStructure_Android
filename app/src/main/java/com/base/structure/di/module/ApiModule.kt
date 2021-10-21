package com.base.structure.di.module

import android.app.Application
import com.base.structure.apiservices.ApiInterface
import com.base.structure.utils.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.base.structure.interceptor.NetworkInterceptor
import com.base.structure.interceptor.RequestInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

import javax.inject.Singleton
import java.io.File
import java.util.concurrent.TimeUnit


@Module
class ApiModule {

    @Provides
    @Singleton
    internal fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        return gsonBuilder.create()
    }

    @Provides
    @Singleton
    internal fun provideCache(application: Application): Cache {
        val cacheSize = (10 * 1024 * 1024).toLong() // 10 MB
        val httpCacheDirectory = File(application.cacheDir, "http-cache")
        return Cache(httpCacheDirectory, cacheSize)
    }


    @Provides
    @Singleton
    internal fun provideNetworkInterceptor(application: Application): NetworkInterceptor {
        return NetworkInterceptor(application.applicationContext)
    }

    @Provides
    @Singleton
    internal fun provideOkhttpClient(cache: Cache, networkInterceptor: NetworkInterceptor): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
        httpClient.cache(cache)
        httpClient.addInterceptor(networkInterceptor)
        httpClient.addInterceptor(logging)
        httpClient.addNetworkInterceptor(RequestInterceptor())
        httpClient.connectTimeout(30, TimeUnit.SECONDS)
        httpClient.readTimeout(30, TimeUnit.SECONDS)
        return httpClient.build()
    }

    @Provides
    @Singleton
    internal fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(Constants.BaseURl)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    internal fun provideAppApiService(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }

 /*   @Provides
    @Singleton
    @Named("youtube_retrofit")
    internal fun provideYoutubeRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(Constants.BaseURlYoutube)
            .client(okHttpClient)
            .build()
    }


    @Provides
    @Singleton
    @Named("youtube_api_service")
    internal fun provideYoutubeAppApiService(@Named("youtube_retrofit") retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }*/

}
