package com.example.matule.domain

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://drbcf26ht2jf.share.zrok.io"

object RetrofitInstance {

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: RetrofitApi by lazy {
        retrofit.create(RetrofitApi::class.java)
    }
}