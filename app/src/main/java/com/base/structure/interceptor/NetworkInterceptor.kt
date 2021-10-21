package com.base.structure.interceptor

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

import java.io.IOException

class NetworkInterceptor(private val context: Context) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        request.newBuilder().header("Cache-Control", "no-cache").build()
       /* if (ConnectivityStatus.isConnected(context)) {
            request.newBuilder()
                .header("Cache-Control", "no-cache")
                .build()
        } else {
            request.newBuilder().header("Cache-Control", "no-cache").build()
        }*/

        return chain.proceed(request)
    }
}
