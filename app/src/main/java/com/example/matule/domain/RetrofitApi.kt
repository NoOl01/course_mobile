package com.example.matule.domain

import com.example.matule.domain.models.requests.Login
import com.example.matule.domain.models.requests.Registration
import com.example.matule.domain.models.requests.UpdateProfile
import com.example.matule.domain.models.responses.AuthModelResult
import com.example.matule.domain.models.responses.CategoryResult
import com.example.matule.domain.models.responses.ErrorResult
import com.example.matule.domain.models.responses.NotificationResult
import com.example.matule.domain.models.responses.ProductInfoResult
import com.example.matule.domain.models.responses.ProductsResult
import com.example.matule.domain.models.responses.ProfileInfoResult
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface RetrofitApi{
    @POST("api/v1/user/register")
    suspend fun register(@Body registrationReq: Registration): AuthModelResult

    @POST("api/v1/user/login")
    suspend fun login(@Body loginReq: Login): AuthModelResult

    @POST("api/v1/user/refresh")
    suspend fun refresh(@Header("X-Refresh-Token") token: String ): AuthModelResult

    @POST("api/v1/user/sendOtp")
    suspend fun sendEmail(@Body email: String): ErrorResult

    @GET("api/v1/category/getAll")
    suspend fun getAllCategories(): CategoryResult

    @GET("api/v1/product/getAll")
    suspend fun getAllProducts(@Header("Authorization") token: String): ProductsResult

    @GET("api/v1/product/getById")
    suspend fun getProductInfo(
        @Header("Authorization") token: String,
        @Query("product_id") productId: Long
    ): ProductInfoResult

    @GET("api/v1/user/getInfo")
    suspend fun getProfileInfo(@Header("Authorization") token: String): ProfileInfoResult

    @Multipart
    @POST("api/v1/user/updateProfileAvatar")
    suspend fun updateProfileAvatar(
        @Header("Authorization") token: String,
        @Part avatar: MultipartBody.Part
    ): ErrorResult

    @POST("api/v1/user/updateProfile")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body updateRequest: UpdateProfile
    ): ProfileInfoResult

    @GET("api/v1/notification/getAll")
    suspend fun getAllNotifications(
        @Header ("Authorization") token: String,
    ): NotificationResult
}