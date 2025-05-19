package com.example.matule.domain

import com.example.matule.domain.models.requests.Login
import com.example.matule.domain.models.requests.Registration
import com.example.matule.domain.models.responses.AuthModelResult
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitApi{
    @POST("api/v1/user/register")
    suspend fun register(@Body registrationReq: Registration): AuthModelResult

    @POST("api/v1/user/login")
    suspend fun login(@Body loginReq: Login): AuthModelResult
}