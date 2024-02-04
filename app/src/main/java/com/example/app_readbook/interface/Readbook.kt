package com.example.app_readbook.`interface`

import com.example.app_readbook.model.CommentPosts
import com.example.app_readbook.model.ReadBook
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface Readbook {
    companion object{
        val gson = GsonBuilder().setDateFormat("dd-MM-yyyy").create()

        val retrofit = Retrofit.Builder()
            .baseUrl(Link.url)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @GET("getReadbook")
    fun getReadbook(): Call<List<ReadBook>>
    @GET("getReadbookUser/{user_id}")
    fun getReadbookUser( @Path("user_id") user_id: String ): Call<List<ReadBook>>


    @GET("getComment/{postId}")
    fun getComment(@Path("postId") postId: String): Call<ReadBook>
}