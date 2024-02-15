package com.example.app_readbook.`interface`

import com.example.app_readbook.model.MessageModel
import com.example.app_readbook.model.User
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface Users {

    companion object{
        val gson = GsonBuilder().setDateFormat("dd-MM-yyyy").create()

        val retrofit = Retrofit.Builder()
            .baseUrl(Link.url)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    data class LoginRequest(val username: String, val password: String)

    data class LoginResponse(val message: String, val token: String,
                             val _id: String,val image: String)

    @POST("login")
    suspend fun loginUser(@Body request: LoginRequest): Response<LoginResponse>

    @GET("getUsersSend/{userID}")
    suspend fun getUser(@Path("userID") userID: String): User

    @GET("getMessage/{userId}")
    fun getMessage(@Path("userId") userId: String): Call<List<MessageModel>>


    data class MessageChat(
        val user_id: User,
        val message: String,
        val timestamp: String
    )
    @GET("getMessage/{userId}")
    fun getChatMessage(@Path("userId") userId: String): Call<List<MessageModel>>
    @GET("getChatMessage/{userId}")
        fun getChatMessageFriend(@Path("userId") userId: String): Call<List<MessageModel>>

    data class MessageData(
        val sender: String,
        val receiver: String,
        val messages: List<Message>
    )

    data class Message(
        val user_id: String,
        val message: String
    )

    data class MessageResponse(
        val message: Message, // Assuming your backend returns the saved message object
        val success: Boolean
    )



    @POST("sendMessage") // Adjust the URL based on your backend
    fun sendMessage(@Body messageData: MessageData): Call<MessageResponse>
}