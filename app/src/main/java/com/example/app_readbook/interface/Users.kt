package com.example.app_readbook.`interface`

import com.google.gson.GsonBuilder
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface Users {

    companion object{
        val gson = GsonBuilder().setDateFormat("dd-MM-yyyy").create()

        val retrofit = Retrofit.Builder()
            .baseUrl(Link.url)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    data class LoginRequest(val username: String, val password: String)

    data class LoginResponse(val message: String, val token: String,val _id: String,)

    @POST("login")
    suspend fun loginUser(@Body request: LoginRequest): Response<LoginResponse>
}