package com.example.login

import com.example.login.models.LogInBody
import com.example.login.models.LogInResponse
import com.example.login.models.RegisterBody
import com.example.login.models.UserDetails
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface API {

    @Headers("Content-Type: application/json")
    @POST("login")
    fun logIn(@Body body: LogInBody): Call<LogInResponse>

    @Headers("Content-Type: application/json")
    @POST("register")
    fun register(@Body body: RegisterBody): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @GET("profile")
    fun getProfile(@Header("Authorization") token: String): Call<UserDetails>
}