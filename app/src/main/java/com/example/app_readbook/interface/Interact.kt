package com.example.app_readbook.`interface`

import com.example.app_readbook.model.ReadBook
import com.example.app_readbook.model.User
import com.google.gson.GsonBuilder
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface Interact {
    companion object{
        val gson = GsonBuilder().setDateFormat("dd-MM-yyyy").create()

        val retrofit = Retrofit.Builder()
            .baseUrl(Link.url)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    data class LikeRequest(
        val user_id: String,
    )
    data class CommentNould(
        val user_id: String,
        val content: String,
//        val image: MultipartBody.Part
    )
    data class CommentApiResponse(
        val message: String,
        val nouvelle: CommentNould // You need to define the Nouvelle data class
    )
    data class LikeCountResponse(val likeCount: Int)


    @POST("likePost/{postId}")
    fun likeNols(
        @Path("postId") postId: String,
        @Body likeRequest: LikeRequest
    ): Call<LikeRequest>

    @POST("addcomment/{nouvelleId}")
    fun addObjImage(
        @Path("nouvelleId") nouvelleId: String,
        @Body commentNould: CommentNould
    ): Call<CommentApiResponse>

    @GET("/countLikes/{postId}")
    fun countLikes(@Path("postId") postId: String): Call<LikeCountResponse>
}