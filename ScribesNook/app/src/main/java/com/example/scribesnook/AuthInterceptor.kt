package com.example.scribesnook

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = Prefs.getToken(context)

        val newRequest = chain.request().newBuilder()

        if (!token.isNullOrEmpty()) {
            newRequest.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(newRequest.build())
    }
}