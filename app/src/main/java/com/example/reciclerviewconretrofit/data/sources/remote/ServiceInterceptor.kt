package com.example.reciclerviewconretrofit.data.sources.remote

import com.example.recyclerviewenhanced.utils.Constants
import okhttp3.Interceptor
import okhttp3.Response






class ServiceInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url.newBuilder()
            .addQueryParameter(Constants.API_KEY, Constants.s1)
            .build()
        val request = chain.request().newBuilder()
            .url(url)
            .build()
        return chain.proceed(request)
    }
}
