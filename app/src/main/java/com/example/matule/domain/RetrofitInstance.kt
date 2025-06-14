package com.example.matule.domain

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://no-ol.tech"

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

object DaDataClient {
    private const val DADATA_BASE_URL = "https://suggestions.dadata.ru"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(DADATA_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val daDataApi: DaDataApi by lazy {
        retrofit.create(DaDataApi::class.java)
    }
}