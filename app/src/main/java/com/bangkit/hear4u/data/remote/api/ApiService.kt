package com.bangkit.hear4u.data.remote.api

import com.bangkit.hear4u.data.remote.response.ArticleResponse
import com.bangkit.hear4u.data.remote.response.DetailArticleResponse
import com.bangkit.hear4u.data.remote.response.LoginResponse
import com.bangkit.hear4u.data.remote.response.RegisterResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("fullname") fullname: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): RegisterResponse

    @GET("articles")
    suspend fun getArticle():ArticleResponse


    @GET("articles/{id}")
    suspend fun getDetailArticles(
        @Path("id") id: String,
    ): DetailArticleResponse
}