package com.base.structure.interceptor


import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class RequestInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url
        val url = originalUrl.newBuilder()
            .build()

        val requestBuilder = originalRequest.newBuilder().url(url)
        val request = requestBuilder
           // .addHeader("language", mPrefs!!.prefHeaderLang)
          //  .addHeader("os", "Android")
         //   .addHeader("version", "1.0.0")
            .build()
        return chain.proceed(request)
    }
}
